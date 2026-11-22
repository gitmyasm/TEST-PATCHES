package app.template.patches.camscanner

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

/** CamScanner modded build supplied for the Morphe patch pipeline. */
val COMPATIBILITY_CAMSCANNER_MOD = Compatibility(
    name = "CamScanner",
    packageName = "com.intsig.camscanner",
    apkFileType = ApkFileType.APK,
    appIconColor = 0x1976D2,
    targets = listOf(
        AppTarget(version = "7.29.5.2611170000"),
    ),
)
