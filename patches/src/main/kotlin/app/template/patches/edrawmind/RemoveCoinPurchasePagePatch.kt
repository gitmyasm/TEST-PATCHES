package app.template.patches.edrawmind

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.returnEarly

/**
 * Blocks navigation to the AI coin purchase page.
 *
 * The special router callback for /buy_ai is short-circuited, so tapping
 * "buy AI dots" no longer opens the recharge screen.
 */
@Suppress("unused")
val removeCoinPurchasePagePatch = bytecodePatch(
    name = "Remove Coin Purchase Page",
    description = "Disables navigation to the AI coin purchase screen.",
) {
    compatibleWith(COMPATIBILITY_EDRAWMIND)

    execute {
        AICoinPurchaseRouteFingerprint.method.returnEarly(true)
    }
}
