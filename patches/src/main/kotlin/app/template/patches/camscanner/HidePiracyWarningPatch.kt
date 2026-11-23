package app.template.patches.camscanner

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.returnEarly

/**
 * The warning methods are skipped while keeping their return values unchanged, so callers
 * stay on the same internal path and features are not re-routed.
 */
@Suppress("unused")
val hidePiracyWarningPatch = bytecodePatch(
    name = "Hide Piracy Warning",
    description = "Removes the local genuine-copy warning dialog and toast without changing the app's internal state.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_CAMSCANNER)

    execute {
        PirateDialogFingerprint.method.returnEarly(true)
        PirateToastFingerprint.method.returnEarly()
    }
}
