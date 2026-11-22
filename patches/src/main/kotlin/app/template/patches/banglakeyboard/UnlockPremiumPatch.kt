package app.template.patches.banglakeyboard

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.packageMetadata
import app.morphe.patcher.patch.patch

/**
 * Unlocks Bangla Keyboard premium features that are decided locally.
 *
 * The premium entitlement is a boolean stream initialized from SharedPreferences
 * and consumed by the app's local checks. The premium screen also receives a
 * boolean result through a callback. This patch makes both values true.
 *
 * Purchases and any server-side account features are not affected.
 */
@Suppress("unused")
val unlockPremiumPatch = patch(
    name = "Unlock Premium",
    description = "Unlocks Bangla Keyboard premium features that are checked locally.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_BANGLA_KEYBOARD)

    execute {
        if (packageMetadata.versionName != "17.7.3") {
            throw PatchException(
                "Unsupported Bangla Keyboard version: ${packageMetadata.versionName}",
            )
        }

        // Instruction 8 is the result of SharedPreferences.getBoolean(...).
        // Replacing it with a constant makes every local check see premium.
        BillingEntitlementFingerprint.method.replaceInstruction(
            8,
            "const/16 v2, 0x1",
        )

        // Instruction 0 casts the callback result to Boolean. Supplying the
        // cached TRUE instance makes the premium UI state active.
        PremiumUiCallbackFingerprint.method.replaceInstruction(
            0,
            "sget-object v6, Ljava/lang/Boolean;->TRUE:Ljava/lang/Boolean;",
        )
    }
}
