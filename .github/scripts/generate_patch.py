"""Generate Morphe patch sources inside TEST-PATCHES from workflow inputs."""
from __future__ import annotations

import json
import os
import re
import shutil
from pathlib import Path

SLUG_RE = re.compile(r"[^a-z0-9]+")
IDENT_RE = re.compile(r"[^A-Za-z0-9_]")


def slug(value: str) -> str:
    return SLUG_RE.sub("_", (value or "").strip().lower()).strip("_") or "patch"


def kotlin_string(value: str) -> str:
    return (str(value or "").replace("\\", "\\\\").replace('"', '\\"')
            .replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t"))


def env(name: str, default: str = "") -> str:
    return os.environ.get(name, default) or default


analysis = json.loads(Path("analysis.json").read_text(encoding="utf-8")) if Path("analysis.json").exists() else {}

app_name = env("INPUT_APP_NAME") or analysis.get("app_name") or "Target App"
package_name = env("INPUT_PACKAGE_NAME") or analysis.get("package") or "com.example.app"
version_name = env("INPUT_VERSION_NAME") or analysis.get("version_name") or ""
patch_name = env("INPUT_PATCH_NAME") or "Generated patch"
description = env("INPUT_PATCH_DESCRIPTION") or f"Generated patch for {app_name}."
mode = env("INPUT_MODE") or "return_early"
target_class = env("INPUT_TARGET_CLASS")
target_method = env("INPUT_TARGET_METHOD")
return_type = env("INPUT_RETURN_TYPE")
parameters = env("INPUT_PARAMETERS")
string_literal = env("INPUT_STRING_LITERAL")
replacement = env("INPUT_REPLACEMENT")
return_value = env("INPUT_RETURN_VALUE") or "void"

color = env("INPUT_APP_ICON_COLOR", "FF0045").lstrip("#")
if not re.fullmatch(r"[0-9A-Fa-f]{6}", color):
    color = "FF0045"

namespace = "app.template"
patch_slug = slug(patch_name)
base = Path("patches/src/main/kotlin") / namespace.replace(".", "/") / "patches" / patch_slug
base.mkdir(parents=True, exist_ok=True)

# The template's example patches are not real patches; keep releases clean.
example = Path("patches/src/main/kotlin/app/template/patches/example")
if example.exists():
    shutil.rmtree(example)

# ---------------------------------------------------------------- Constants
targets = (
    [f'        AppTarget(version = "{kotlin_string(version_name)}")']
    if version_name else ['        AppTarget(version = null, isExperimental = true)']
)
constants = f'''package {namespace}.patches.{patch_slug}

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {{
    val COMPATIBILITY = Compatibility(
        name = "{kotlin_string(app_name)}",
        packageName = "{kotlin_string(package_name)}",
        appIconColor = 0x{color.upper()},
        targets = listOf(
{chr(10).join(targets)}
        )
    )
}}
'''

# -------------------------------------------------------------- Fingerprint
fingerprint_args: list[str] = []
if target_class:
    fingerprint_args.append(f'    definingClass = "{kotlin_string(target_class)}",')
if target_method:
    fingerprint_args.append(f'    name = "{kotlin_string(target_method)}",')
if return_type:
    fingerprint_args.append(f'    returnType = "{kotlin_string(return_type)}",')
params = [p.strip() for p in parameters.split(",") if p.strip()]
if params:
    fingerprint_args.append("    parameters = listOf(" + ", ".join(f'"{kotlin_string(p)}"' for p in params) + "),")

use_string_filter = bool(string_literal) and mode != "replace_string"
if use_string_filter:
    fingerprint_args.append(f'    filters = listOf(string("{kotlin_string(string_literal)}")),')

fingerprint_imports = ["import app.morphe.patcher.Fingerprint"]
if use_string_filter:
    fingerprint_imports.append("import app.morphe.patcher.string")

warning = "" if fingerprint_args else \
    "    // TODO: add definingClass, name or a string filter to make this fingerprint specific.\n"
fingerprints = f'''package {namespace}.patches.{patch_slug}

{chr(10).join(fingerprint_imports)}

object TargetFingerprint : Fingerprint(
{warning}{chr(10).join(fingerprint_args)}
)
'''

# ------------------------------------------------------------------ Execute
if mode in ("return_early", "return_late"):
    function = "returnEarly" if mode == "return_early" else "returnLate"
    ret = return_value.strip().lower()
    if ret in ("", "void", "v"):
        call = f"{function}()"
    elif ret in ("null", "none"):
        call = f"{function}(null)"
    else:
        call = f"{function}({return_value.strip()})"
    body = f"TargetFingerprint.method.{call}"
    imports = [f"import app.morphe.patcher.util.{function}"]
elif mode == "insert_smali":
    imports = ["import app.morphe.patcher.extensions.InstructionExtensions.addInstructions"]
    smali = "\n".join("                " + line for line in replacement.splitlines())
    body = f'''TargetFingerprint.method.addInstructions(
                0,
                """
{smali}
                """
            )'''
elif mode == "replace_string":
    imports = [
        "import app.morphe.patcher.util.replaceStrings",
        "import app.morphe.patcher.string",
    ]
    body = f'''replaceStrings(
                string("{kotlin_string(string_literal)}") to "{kotlin_string(replacement)}"
            )'''
else:
    imports = ["import app.morphe.patcher.patch.PatchException"]
    body = "// TODO: implement the patch logic for this target."

# --------------------------------------------------------------- Patch file
words = [w for w in re.split(r"[^A-Za-z0-9]+", patch_name) if w]
if not words:
    words = ["Generated", "Patch"]
value = "".join(w[0].upper() + w[1:] for w in words)
if value[0].isdigit():
    value = "P" + value
val_name = value[0].lower() + value[1:]
if val_name in ("class", "val", "fun", "object", "return"):
    val_name = "generatedPatch"

all_imports = sorted(set(imports + ["import app.morphe.patcher.patch.patch"]))
patch = f'''package {namespace}.patches.{patch_slug}

{chr(10).join(all_imports)}

@Suppress("unused")
val {val_name} = patch(
    name = "{kotlin_string(patch_name)}",
    description = "{kotlin_string(description)}",
    default = true
) {{
    compatibleWith(Constants.COMPATIBILITY)

    execute {{
        {body}
    }}
}}
'''

sources = {
    base / "Constants.kt": constants,
    base / "Fingerprints.kt": fingerprints,
    base / "Patch.kt": patch,
}
for path, content in sources.items():
    path.write_text(content, encoding="utf-8")

info = {
    "patch_name": patch_name,
    "package": package_name,
    "app_name": app_name,
    "version_name": version_name,
    "mode": mode,
    "target_class": target_class,
    "target_method": target_method,
    "generated_files": [str(p) for p in sources],
}
Path("patch-info.json").write_text(json.dumps(info, indent=2), encoding="utf-8")

for path in sources:
    print(f"--- {path} ---")
    print(sources[path])
