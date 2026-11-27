package app.template.patches.paytm

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.returnEarly

/**
 * Disables local Paytm ad view rendering.
 *
 * This targets a concrete local ad rendering entry point (PaytmAdView JSON).
 * It may not remove all ads/promotions across the app or server-delivered content.
 */
@Suppress("unused")
val disableAdsPatch = bytecodePatch(
    name = "Disable Ads",
    description = "Disables local Paytm ad view rendering.",
) {
    compatibleWith(COMPATIBILITY_PAYTM)

    execute {
        PaytmAdViewSetAdDataJsonFingerprint.method.returnEarly()
    }
}
