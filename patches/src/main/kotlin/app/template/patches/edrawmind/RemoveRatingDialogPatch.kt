package app.template.patches.edrawmind

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.returnEarly

/**
 * Prevents the in-app rating prompt from being shown.
 *
 * The app's rating counter is updated just before the dialog is displayed;
 * disabling this entry point stops that flow entirely.
 */
@Suppress("unused")
val removeRatingDialogPatch = bytecodePatch(
    name = "Remove Rating Dialog",
    description = "Disables the in-app rating prompt and its counter flow.",
) {
    compatibleWith(COMPATIBILITY_EDRAWMIND)

    execute {
        RatingDialogTriggerFingerprint.method.returnEarly()
    }
}
