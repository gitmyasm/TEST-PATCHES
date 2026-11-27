package app.template.patches.tempmail

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.returnEarly

/**
 * Bypasses the local Pairip license-check trigger.
 *
 * This prevents the local Play Store/paywall redirect path. It does not claim
 * to bypass a remote license service or server-side entitlement check.
 */
@Suppress("unused")
val bypassTempMailLicenseCheckPatch = bytecodePatch(
    name = "Bypass Temp Mail License Check",
    description = "Bypasses the local Pairip license-check trigger that can redirect to Play Store.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_TEMP_MAIL)

    execute {
        PairipLicenseCheckFingerprint.method.returnEarly()
    }
}
