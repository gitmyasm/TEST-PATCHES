package app.template.patches.instantmail

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

/** Instant Mail (com.temporary.email.pro). */
val COMPATIBILITY_INSTANT_MAIL = Compatibility(
    name = "Instant Mail",
    packageName = "com.temporary.email.pro",
    apkFileType = ApkFileType.APK,
    appIconColor = 0x3F51B5,
    targets = listOf(
        AppTarget(version = "8.0.0"),
    ),
)
