# Cloud reversing — how to use

Everything runs on GitHub's servers. Nothing needs to be installed on your phone.

## 1. Run the analysis

1. Open **https://github.com/gitmyasm/TEST-PATCHES/actions**
2. Click **Analyze APK** in the left sidebar
3. Click **Run workflow** → choose branch `dev`
4. Fill in the form:

| Field | What to put |
| --- | --- |
| `apk_url` | Direct public link to the `.apk` file |
| `patch_name` | Name shown in Morphe (e.g. `Remove Ads`) |
| `app_name` | Leave blank to auto-detect from the APK |
| `package_name` | Leave blank to auto-detect |
| `version_name` | Leave blank to auto-detect |
| `mode` | `return_early` (disable a method) is the usual choice |
| `target_class` | Class to patch, e.g. `Lcom/app/AdsManager;` |
| `target_method` | Method name, e.g. `loadAd` |
| `return_type` | `V` for void, `Z` for boolean, etc. |
| `parameters` | Method parameters, comma separated |
| `string_literal` | Text to search for, if you don't know the class |
| `push_patch` | Leave ticked to publish a test build |

5. Click **Run workflow**. It takes roughly 3–6 minutes.

## 2. Get the results

When the run finishes, scroll to the bottom of the run page:

- **Artifact `reversing-results`** contains:
  - `analysis.json` — package, version, SDK, permissions, activities, classes, SHA-256
  - `jadx-sources.zip` — decompiled Java
  - `apktool-sources.zip` — smali + resources
  - `generated-patches.zip` — the generated Morphe patch sources
- The run page also shows a short summary table.

## 3. Test the patch

If `push_patch` was ticked, the workflow commits `feat: Add <patch name>` to `dev`
and starts a Release run automatically. Wait ~2 minutes, then download the new
`.mpp` from **Releases** and test it in Morphe Manager.

## 4. Promote it

When a patch works, tell me **"move <patch name> to SM Patches"**. I will copy it
to `SM-PATCHES`, open `dev → main` PR, and publish a stable release.

## Trigger from a terminal (optional)

```bash
gh workflow run analyze.yml \
  -R gitmyasm/TEST-PATCHES \
  -f apk_url="https://example.com/app.apk" \
  -f patch_name="Remove Ads" \
  -f target_class="Lcom/app/AdsManager;" \
  -f target_method="loadAd" \
  -f return_type="V"
```

## Notes

- The APK URL must be publicly reachable from GitHub's servers.
- Artifacts are kept for 30 days.
- If Jadx or Apktool cannot handle an APK, the workflow still finishes and
  uploads whatever it produced.
- Generated fingerprints are a starting point. If the pre-release build fails,
  paste the error here and I'll fix the Kotlin.
