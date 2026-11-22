package app.template.patches.nicemind

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

/** Nice Mind Map (mind.map.mindmap). */
val COMPATIBILITY_NICE_MIND = Compatibility(
    name = "Nice Mind",
    packageName = "mind.map.mindmap",
    apkFileType = ApkFileType.APK,
    appIconColor = 0x3F51B5,
    targets = listOf(
        AppTarget(version = "12.8.0"),
        AppTarget(version = "12.7.0"),
    ),
)
