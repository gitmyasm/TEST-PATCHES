package app.template.patches.regain

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch

/**
 * Removes Regain's local setup-time promotional offer prompt.
 *
 * This only forces the app-side "show unlock offer text" flag off. It does not
 * change any remote configuration, offering, discount, purchase, or server-side
 * entitlement state.
 */
@Suppress("unused")
val removeSetupPromotionalOfferPatch = bytecodePatch(
    name = "Remove Promotional Offer During Setup",
    description = "Forces Regain's setup unlock-offer prompt off. Remote offers and purchases are unchanged.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_REGAIN)

    execute {
        SetupOfferStateBuilderFingerprint.method.addInstructions(
            90,
            "sget-object v6, Ljava/lang/Boolean;->FALSE:Ljava/lang/Boolean;",
        )
    }
}
