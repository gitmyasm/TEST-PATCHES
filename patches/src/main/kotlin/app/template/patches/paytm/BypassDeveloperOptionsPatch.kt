package app.template.patches.paytm

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.returnEarly

/**
 * Bypasses local Paytm developer-options and USB/ADB debugging checks.
 *
 * This targets concrete local settings checks only. It does not cover other
 * integrity, attestation, emulator, or server-side security controls.
 */
@Suppress("unused")
val bypassDeveloperOptionsPatch = bytecodePatch(
    name = "Bypass Developer Options",
    description = "Bypasses local Paytm developer-options and USB debugging checks.",
) {
    compatibleWith(COMPATIBILITY_PAYTM)

    execute {
        DeveloperOptionsEnabledFingerprint.method.returnEarly(false)
        UsbDebuggingEnabledFingerprint.method.returnEarly(false)
    }
}
