package app.template.patches.tempmail

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.returnEarly

/**
 * Disables Temp Mail's local Play In-App Update check.
 *
 * The startup check, resume recheck, and local version comparison are made
 * inert. Remote release metadata is not changed.
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
        InAppUpdateResumeFingerprint.method.returnEarly()
        InAppUpdateNeededFingerprint.method.returnEarly(false)
    }
}
