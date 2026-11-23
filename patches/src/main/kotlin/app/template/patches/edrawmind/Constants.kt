package app.template.patches.edrawmind

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

/** EdrawMind (com.edrawsoft.mindmasterg). */
val COMPATIBILITY_EDRAWMIND = Compatibility(
    name = "EdrawMind",
    packageName = "com.edrawsoft.mindmasterg",
    apkFileType = ApkFileType.APK,
    appIconColor = 0x1A73E8,
    targets = listOf(
        AppTarget(version = "7.10.15"),
    ),
)
