package app.template.patches.paytm

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.returnEarly

/**
 * Bypasses a local Paytm/Protectt Frida hook detection check.
 *
 * This targets a concrete local hook-detection aggregator. It does not cover
 * every native/runtime anti-hook, emulator, root, or integrity signal.
 */
@Suppress("unused")
val bypassFridaDetectionPatch = bytecodePatch(
    name = "Bypass Frida Detection",
    description = "Bypasses a local Paytm Frida hook detection check.",
) {
    compatibleWith(COMPATIBILITY_PAYTM)

    execute {
        FridaHookDetectionFingerprint.method.returnEarly(false)
    }
}
