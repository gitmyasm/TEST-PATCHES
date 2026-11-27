package app.template.patches.coolmind

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

/** Cool Mind (mindmap.mind.cool). */
val COMPATIBILITY_COOL_MIND = Compatibility(
    name = "Cool Mind",
    packageName = "mindmap.mind.cool",
    apkFileType = ApkFileType.APK,
    appIconColor = 0x3F51B5,
    targets = listOf(
        AppTarget(version = "4.0.0"),
    ),
)
