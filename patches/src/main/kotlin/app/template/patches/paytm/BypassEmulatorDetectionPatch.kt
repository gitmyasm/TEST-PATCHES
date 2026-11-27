package app.template.patches.paytm

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.returnEarly

/**
 * Bypasses a local Protectt emulator-detection rule check.
 *
 * This targets a concrete local emulator rule handler only. It does not cover
 * all emulator/root/integrity signals or any server-side attestation/fraud checks.
 */
@Suppress("unused")
val bypassEmulatorDetectionPatch = bytecodePatch(
    name = "Bypass Emulator Detection",
    description = "Bypasses a local Protectt emulator-detection rule check.",
) {
    compatibleWith(COMPATIBILITY_PAYTM)

    execute {
        EmulatorDetectionRuleFingerprint.method.returnEarly()
    }
}
