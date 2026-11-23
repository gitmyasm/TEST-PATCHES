package app.template.patches.paytm

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.returnEarly

/**
 * Bypasses Pairip's local APK signature verification.
 *
 * This only affects the local signature check. It does not guarantee bypass
 * of server-side integrity checks or other runtime protections.
 */
@Suppress("unused")
val bypassSignatureCheckPatch = bytecodePatch(
    name = "Bypass Signature Check",
    description = "Bypasses the local Pairip APK signature verification check.",
) {
    compatibleWith(COMPATIBILITY_PAYTM)

    execute {
        PairipSignatureVerifyFingerprint.method.returnEarly()
        PairipSignatureMatchesFingerprint.method.returnEarly(true)
    }
}
