package app.template.patches.camscanner

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.returnEarly

@Suppress("unused")
val unlockPremiumPatch = bytecodePatch(
    name = "Unlock Premium",
    description = "Reports local VIP status and a far-future expiry. Server-validated work remains remote.",
) {
    compatibleWith(COMPATIBILITY_CAMSCANNER)

    execute {
        // 1 is the local VIP status code; the stored free default is 2.
        VipStatusCodeFingerprint.method.returnEarly(1L)
        VipExpiryFingerprint.method.returnEarly(9999999999L)
    }
}
