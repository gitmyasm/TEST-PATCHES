package app.template.patches.banglakeyboard

import app.morphe.patcher.patch.deleteResource
import app.morphe.patcher.patch.listApkEntries
import app.morphe.patcher.patch.patch

private val JUNK_ACTIVITIES = setOf(
    "com.deshkeyboard.promotedtiles.testing.TilesTestingActivity",
    "com.deshkeyboard.promotedtiles.testing.UserDictViewerActivity",
    "com.deshkeyboard.shortcuts.diagnosticinfo.DiagnosticInfoActivity",
    "com.example.android.softkeyboard.easyconfig.EasyConfig",
)

private val JUNK_FILES = setOf(
    "DebugProbesKt.bin",
    "assets/dexopt/baseline.prof",
    "assets/dexopt/baseline.profm",
)

/** Removes debug screens and build artifacts that are not needed at runtime. */
@Suppress("unused")
val removeJunkPatch = patch(
    name = "Remove Unnecessary Garbage",
    description = "Removes testing/debug screens and build-only files from Bangla Keyboard.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_BANGLA_KEYBOARD)

    execute {
        editManifest {
            removeComponents("activity", JUNK_ACTIVITIES)
        }

        JUNK_FILES.forEach { deleteResource(it) }
        listApkEntries()
            .filter { it.startsWith("META-INF/") && it.endsWith(".version") }
            .forEach { deleteResource(it) }
    }
}
