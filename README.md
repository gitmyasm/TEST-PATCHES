# TEST PATCHES

Staging repository for new Morphe patches by [gitmyasm](https://github.com/gitmyasm).
Test each patch here manually first. Once confirmed, ask ReverseKit to promote the patch
into [SM-PATCHES](https://github.com/gitmyasm/SM-PATCHES).

## Add this source to Morphe Manager

`https://github.com/gitmyasm/TEST-PATCHES`

[![Add to Morphe](.github/assets/add-to-morphe.svg)](https://morphe.software/add-source?github=gitmyasm/TEST-PATCHES&name=Test%20Patches)

## Patches

<!-- PATCHES_START EXPANDED -->
> **[v1.0.0-dev.88](https://github.com/gitmyasm/TEST-PATCHES/releases/tag/v1.0.0-dev.88)**&nbsp;&nbsp;&middot;&nbsp;&nbsp;`dev`&nbsp;&nbsp;&middot;&nbsp;&nbsp;**44 patches** across **5 apps**&nbsp;&nbsp;&middot;&nbsp;&nbsp;[Full details](PATCHES.md)

| # | App | Patches | Version | Package |
|---|---|---|---|---|
| 1 | [**Cool Mind**](PATCHES.md#cool-mind-mindmapmindcool) | 3 | `4.0.0` | [`mindmap.mind.cool`](https://play.google.com/store/apps/details?id=mindmap.mind.cool) |
| 2 | [**Noji**](PATCHES.md#noji-comvedasappsflashcards) | 4 | `26.44.0` | [`com.vedasapps.flashcards`](https://play.google.com/store/apps/details?id=com.vedasapps.flashcards) |
| 3 | [**Paytm**](PATCHES.md#paytm-netone97paytm) | 9 | `10.86.13` | [`net.one97.paytm`](https://play.google.com/store/apps/details?id=net.one97.paytm) |
| 4 | [**Temp Mail**](PATCHES.md#temp-mail-comtempmail) | 7 | `4.12` | [`com.tempmail`](https://play.google.com/store/apps/details?id=com.tempmail) |
| 5 | [**WPS Office**](PATCHES.md#wps-office-cnwpsmoffice_eng) | 21 | `26.11.1` | [`cn.wps.moffice_eng`](https://play.google.com/store/apps/details?id=cn.wps.moffice_eng) |
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
