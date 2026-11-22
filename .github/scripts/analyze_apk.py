"""Static APK inspection used by the Analyze APK GitHub workflow."""
from __future__ import annotations

import hashlib
import json
import os
from pathlib import Path

try:
    from androguard.core.apk import APK
except ImportError:  # older androguard
    from androguard.core.bytecodes.apk import APK

apk_path = Path("target.apk")
if not apk_path.exists():
    raise SystemExit("target.apk was not downloaded")

raw = apk_path.read_bytes()
apk = APK(str(apk_path))

info: dict = {
    "package": apk.get_package(),
    "version_name": apk.get_androidversion_name(),
    "version_code": apk.get_androidversion_code(),
    "min_sdk": apk.get_min_sdk_version(),
    "target_sdk": apk.get_target_sdk_version(),
    "app_name": apk.get_app_name(),
    "main_activity": apk.get_main_activity(),
    "permissions": list(apk.get_permissions() or []),
    "activities": list(apk.get_activities() or []),
    "services": list(apk.get_services() or []),
    "receivers": list(apk.get_receivers() or []),
    "size": len(raw),
    "sha256": hashlib.sha256(raw).hexdigest(),
}

classes: list[str] = []
try:
    from androguard.core.dex import DEXParser

    dex = apk.get_dex()
    if isinstance(dex, (list, tuple)):
        dex = dex[0] if dex else b""
    parser = DEXParser(dex)
    classes = [c.get_name() for c in parser.get_classes()]
except Exception as exc:  # analysis must not fail because of one malformed dex
    info["dex_error"] = str(exc)

info["class_count"] = len(classes)
info["classes"] = classes[:5000]

Path("analysis.json").write_text(json.dumps(info, indent=2, ensure_ascii=False), encoding="utf-8")

env_file = os.environ.get("GITHUB_ENV")
if env_file:
    with open(env_file, "a", encoding="utf-8") as handle:
        handle.write(f"DETECTED_PACKAGE={info['package'] or ''}\n")
        handle.write(f"DETECTED_VERSION={info['version_name'] or ''}\n")
        handle.write(f"DETECTED_APP_NAME={info['app_name'] or ''}\n")

print(json.dumps({k: info[k] for k in (
    "package", "version_name", "version_code", "min_sdk", "target_sdk",
    "app_name", "main_activity", "class_count", "size", "sha256",
)}, indent=2))
