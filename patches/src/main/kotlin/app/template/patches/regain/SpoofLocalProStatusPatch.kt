package app.template.patches.regain

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.returnEarly

/**
 * Forces Regain's local "is Pro user" checks to report true.
 *
 * This only changes local app-side gating. It does not create or authorize a
 * RevenueCat, Google Play, Razorpay, or Regain server subscription, and it does
 * not prove that server-backed entitlements will be accepted.
 */
@Suppress("unused")
val spoofLocalProStatusPatch = bytecodePatch(
    name = "Spoof Local Pro Status",
    description = "Forces Regain's local Pro-user gates to return true. Server entitlements are unchanged.",
) {
    compatibleWith(COMPATIBILITY_REGAIN)

    execute {
        ProUserUseCaseFingerprint.method.returnEarly(true)
        SubscriptionManagerProCheckFingerprint.method.returnEarly(true)
    }
}
