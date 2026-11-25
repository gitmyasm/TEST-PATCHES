package app.template.patches.regain

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.returnEarly

/**
 * Disables Regain's local in-house ad view-model handlers.
 *
 * This targets the local in-house ad screen state machine only. It does not
 * change server-side ad configuration, remote URLs, or any third-party SDK
 * behaviour.
 */
@Suppress("unused")
val removeInHouseAdsPatch = bytecodePatch(
    name = "Remove In-House Ads",
    description = "Disables Regain's local in-house ad screen state machine.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_REGAIN)

    execute {
        InHouseAdEventHandlerFingerprint.method.returnEarly()
        InHouseAdStateHandlerFingerprint.method.returnEarly()
    }
}
