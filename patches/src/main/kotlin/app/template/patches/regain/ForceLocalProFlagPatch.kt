package app.template.patches.regain

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch

/**
 * Forces Regain's local subscription-state Pro flag on.
 *
 * This only changes the app-side Pro flag returned by the local subscription
 * state model. It does not create or authorize a RevenueCat, Google Play,
 * Razorpay, or Regain server subscription, and it does not prove that
 * server-backed entitlements will be accepted.
 */
@Suppress("unused")
val forceLocalProFlagPatch = bytecodePatch(
    name = "Force Local Pro Flag In Subscription State",
    description = "Forces Regain's local subscription-state Pro flag on. Purchases and server entitlements are unchanged.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_REGAIN)

    execute {
        SubscriptionStateProFlagFingerprint.method.addInstructions(
            0,
            """
                const/4 v0, 0x1
                return v0
            """.trimIndent(),
        )
    }
}
