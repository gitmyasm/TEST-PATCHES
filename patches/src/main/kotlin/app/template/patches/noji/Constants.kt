package app.template.patches.noji

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

/** Noji (com.vedasapps.flashcards). */
val COMPATIBILITY_NOJI = Compatibility(
    name = "Noji",
    packageName = "com.vedasapps.flashcards",
    apkFileType = ApkFileType.APKS,
    appIconColor = 0x6A1B9A,
    targets = listOf(
        AppTarget(version = "26.44.0"),
    ),
)
