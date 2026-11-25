package app.template.patches.tempmail

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

/** Temp Mail (com.tempmail). */
val COMPATIBILITY_TEMP_MAIL = Compatibility(
    name = "Temp Mail",
    packageName = "com.tempmail",
    apkFileType = ApkFileType.APK,
    appIconColor = 0x18CA88,
    targets = listOf(
        AppTarget(version = "4.12"),
    ),
)
