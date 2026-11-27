package app.template.patches.tempmail

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.returnEarly

/**
 * Disables local ad loading at the central AdManager gate.
 *
 * AdsIntegrityGate.f() is consulted by AdManager and the app-open observer,
 * while AdManager.f() gates the remaining local ad loaders. Returning false
 * from both prevents local ad loads across all formats. Remote ad
 * configuration and server-delivered content are not modified.
 */
@Suppress("unused")
val removeTempMailAdsPatch = bytecodePatch(
    name = "Remove Temp Mail Ads",
    description = "Disables Temp Mail's local ad loading gate for all ad formats.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_TEMP_MAIL)

    execute {
        AdsIntegrityGateFingerprint.method.returnEarly(false)
        AdManagerCanRequestAdsFingerprint.method.returnEarly(false)
    }
}
