# TEST PATCHES

Staging repository for new Morphe patches by [gitmyasm](https://github.com/gitmyasm).
Test each patch here manually first. Once confirmed, ask ReverseKit to promote the patch
into [SM-PATCHES](https://github.com/gitmyasm/SM-PATCHES).

## Add this source to Morphe Manager

`https://github.com/gitmyasm/TEST-PATCHES`

[![Add to Morphe](.github/assets/add-to-morphe.svg)](https://morphe.software/add-source?github=gitmyasm/TEST-PATCHES&name=Test%20Patches)

## Patches

<!-- PATCHES_START EXPANDED -->
> **[v1.0.0-dev.18](https://github.com/gitmyasm/TEST-PATCHES/releases/tag/v1.0.0-dev.18)**&nbsp;&nbsp;&middot;&nbsp;&nbsp;`dev`&nbsp;&nbsp;&middot;&nbsp;&nbsp;**16 patches** across **4 apps**&nbsp;&nbsp;&middot;&nbsp;&nbsp;[Full details](PATCHES.md)

| # | App | Patches | Version | Package |
|---|---|---|---|---|
| 1 | [**Bangla Keyboard**](PATCHES.md#bangla-keyboard-combanglakeyboardforandroid) | 4 | `17.7.3` | [`com.bangla.keyboard.for.android`](https://play.google.com/store/apps/details?id=com.bangla.keyboard.for.android) |
| 2 | [**CamScanner**](PATCHES.md#camscanner-comintsigcamscanner) | 7 | `7.27.0.2609220000` | [`com.intsig.camscanner`](https://play.google.com/store/apps/details?id=com.intsig.camscanner) |
| 3 | [**Nice Mind**](PATCHES.md#nice-mind-mindmapmindmap) | 1 | `12.8.0`, `12.7.0` | [`mind.map.mindmap`](https://play.google.com/store/apps/details?id=mind.map.mindmap) |
| 4 | [**Noji**](PATCHES.md#noji-comvedasappsflashcards) | 4 | `26.44.0` | [`com.vedasapps.flashcards`](https://play.google.com/store/apps/details?id=com.vedasapps.flashcards) |
<!-- PATCHES_END -->

## Cloud reversing (no local tools needed)

Paste an APK URL into the **Analyze APK** action and let GitHub do the decompiling,
analysis and patch generation. See [AUTOMATION.md](AUTOMATION.md) for the full guide.

## Workflow

1. Generate a new patch with the **Analyze APK** action and push it to `dev` here (`feat:` commit).
2. Download the pre-release `.mpp` from Releases and test it on the target app.
3. When it works, tell ReverseKit: "move <patch> to SM Patches".
4. ReverseKit copies the patch sources into `SM-PATCHES` on `dev`, then merges to `main`
   for a stable release. The tested patch stays here too unless you ask to remove it.

## Development

```bash
./gradlew buildAndroid
# patches/build/libs/patches-*.mpp
```
