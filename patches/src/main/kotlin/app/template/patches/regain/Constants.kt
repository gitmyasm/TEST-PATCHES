package app.template.patches.regain

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

/** Regain (ai.regainapp). */
val COMPATIBILITY_REGAIN = Compatibility(
    name = "Regain",
    packageName = "ai.regainapp",
    apkFileType = ApkFileType.APK,
    appIconColor = 0x6C4DF6,
    targets = listOf(
        AppTarget(version = "76.1.2139"),
    ),
)
