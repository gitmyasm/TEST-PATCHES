package app.template.patches.tempmail

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.returnEarly

/**
 * Disables local ad loading at the central AdManager gate.
 *
 * AdManager.f() is consulted before loading interstitial, rewarded, banner,
 * native and app-open ads. Returning false prevents those local ad loads.
 * Remote ad configuration and server-delivered content are not modified.
 */
@Suppress("unused")
val removeTempMailAdsPatch = bytecodePatch(
    name = "Remove Temp Mail Ads",
    description = "Disables Temp Mail's local ad loading gate for all ad formats.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_TEMP_MAIL)

    execute {
        AdManagerCanRequestAdsFingerprint.method.returnEarly(false)
    }
}
