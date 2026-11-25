package app.template.patches.tempmail

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.returnEarly

/**
 * Disables the local checks that can redirect the patched app to Play Store.
 *
 * The startup VM hook, signature integrity check, and license-check trigger
 * are made inert. Remote license, integrity, and entitlement services are not
 * modified.
 */
@Suppress("unused")
val bypassTempMailPlayStoreRedirectPatch = bytecodePatch(
    name = "Bypass Temp Mail Play Store Redirect",
    description = "Disables local Pairip startup, signature, and license checks that can redirect to Play Store.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_TEMP_MAIL)

    execute {
        PairipStartupLaunchFingerprint.method.returnEarly()
        PairipSignatureVerifyFingerprint.method.returnEarly()
        PairipLicenseCheckFingerprint.method.returnEarly()
    }
}
