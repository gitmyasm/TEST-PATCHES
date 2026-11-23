package app.template.patches.camscanner

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

/** CamScanner 7.27.0.2609220000 (versionCode 72701). A full APKM bundle is required. */
val COMPATIBILITY_CAMSCANNER = Compatibility(
    name = "CamScanner",
    packageName = "com.intsig.camscanner",
    apkFileType = ApkFileType.APKM_REQUIRED,
    appIconColor = 0x19BCAA,
    targets = listOf(
        AppTarget("7.27.0.2609220000", versionCode = 72701),
    ),
)
