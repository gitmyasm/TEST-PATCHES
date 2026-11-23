package app.template.patches.banglakeyboard

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.packageMetadata
import app.morphe.patcher.patch.patch

/**
 * Hides the promoted app-suggestion strip above the keyboard.
 *
 * The strip is a lazy view that starts gone in the input layout. Its controller
 * is the only code that makes the view visible, so skipping that single call
 * keeps the row hidden. Normal dictionary and word suggestions are untouched.
 */
@Suppress("unused")
val removePromotedSuggestionsPatch = patch(
    name = "Remove Promoted Suggestions",
    description = "Hides the promoted app-suggestion row above the keyboard.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_BANGLA_KEYBOARD)

    execute {
        if (packageMetadata.versionName != "17.7.3") {
            throw PatchException(
                "Unsupported Bangla Keyboard version: ${packageMetadata.versionName}",
            )
        }

        // Instruction 67 is the sole setVisibility(VISIBLE) call for the
        // promoted-tiles lazy view. The layout already starts with it gone.
        PromotedTilesVisibilityFingerprint.method.replaceInstruction(67, "nop")
    }
}
