package app.template.patches.tempmail

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.returnEarly

/**
 * Disables Temp Mail's local Play In-App Update check.
 *
 * The startup check is made inert and the local version comparison always
 * reports that no update is needed. Remote release metadata is not changed.
 */
@Suppress("unused")
val disableTempMailUpdateCheckPatch = bytecodePatch(
    name = "Disable Temp Mail Update Check",
    description = "Disables Temp Mail's local startup and version comparison update checks.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_TEMP_MAIL)

    execute {
        InAppUpdateStartFingerprint.method.returnEarly()
        InAppUpdateNeededFingerprint.method.returnEarly(false)
    }
}
