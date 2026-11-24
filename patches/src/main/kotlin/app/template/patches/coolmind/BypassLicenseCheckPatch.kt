package app.template.patches.coolmind

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.returnEarly

/**
 * Bypasses the local Pairip license check entry point.
 *
 * This targets the local license-check trigger that can show the Play
 * Store/paywall flow. It does not guarantee bypass of server-side license or
 * integrity checks.
 */
@Suppress("unused")
val bypassCoolMindLicenseCheckPatch = bytecodePatch(
    name = "Bypass Cool Mind License Check",
    description = "Bypasses the local Pairip license check that can redirect to Play Store.",
) {
    compatibleWith(COMPATIBILITY_COOL_MIND)

    execute {
        CoolMindLicenseCheckFingerprint.method.returnEarly()
    }
}
