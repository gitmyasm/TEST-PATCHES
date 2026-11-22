package app.template.patches.banglakeyboard

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

/** Bangla Keyboard for Android. */
val COMPATIBILITY_BANGLA_KEYBOARD = Compatibility(
    name = "Bangla Keyboard",
    packageName = "com.bangla.keyboard.for.android",
    apkFileType = ApkFileType.APK,
    appIconColor = 0x1976D2,
    targets = listOf(
        AppTarget(version = "17.7.3"),
    ),
)
