package app.template.patches.paytm

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.returnEarly

/**
 * Bypasses the local Pairip license check entry point.
 *
 * This targets the local license-check trigger that can redirect users to the
 * Play Store/paywall flow. It does not guarantee bypass of server-side license
 * or integrity checks.
 */
@Suppress("unused")
val bypassLicenseCheckPatch = bytecodePatch(
    name = "Bypass License Check",
    description = "Bypasses the local Pairip license check that can redirect to Play Store.",
) {
    compatibleWith(COMPATIBILITY_PAYTM)

    execute {
        PairipLicenseCheckFingerprint.method.returnEarly()
    }
}
