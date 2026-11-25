package app.template.patches.tempmail

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.returnEarly

/**
 * Bypasses the local Pairip APK signature verification.
 *
 * Patching changes the signing identity, so the embedded signature check can
 * otherwise reject the app. This does not alter any server-side integrity
 * decision.
 */
@Suppress("unused")
val bypassTempMailSignatureCheckPatch = bytecodePatch(
    name = "Bypass Temp Mail Signature Check",
    description = "Bypasses the local Pairip APK signature verification.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_TEMP_MAIL)

    execute {
        PairipSignatureVerifyFingerprint.method.returnEarly()
        PairipSignatureMatchesFingerprint.method.returnEarly(true)
    }
}
