package app.template.patches.paytm

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

/** Paytm (net.one97.paytm). */
val COMPATIBILITY_PAYTM = Compatibility(
    name = "Paytm",
    packageName = "net.one97.paytm",
    apkFileType = ApkFileType.APK,
    appIconColor = 0x00B9F5,
    targets = listOf(
        AppTarget(version = "10.86.13"),
    ),
)
