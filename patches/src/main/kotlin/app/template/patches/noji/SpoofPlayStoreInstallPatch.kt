package app.template.patches.noji

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch

/**
 * Treats a sideloaded installation as a Play Store installation.
 *
 * Pairip initializes its local installer result register to false and only sets
 * it to true after the installer package matches the store. Initializing that
 * register to true keeps the surrounding logging, branches, and exception
 * handling intact while making the local check pass. The device-info accessor
 * is also pointed at the store package so any app-level install-source logic
 * sees the same answer. This patch is independent of the premium, ads, and
 * trackers patches.
 */
@Suppress("unused")
val spoofPlayStoreInstallPatch = bytecodePatch(
    name = "Spoof Play Store Install",
    description = "Treats sideloaded Noji installations as Play Store installations.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_NOJI)

    execute {
        PairipLocalInstallerCheckFingerprint.method.apply {
            // Instruction 1 initializes the boolean result returned by the check.
            replaceInstruction(1, "const/4 v1, 1")
        }

        DeviceInstallerPackageFingerprint.method.apply {
            // Instruction 8 reads the installer; 9 stores that result. Replace
            // both so every path returns the store installer value.
            replaceInstruction(8, "const-string v0, \"com.android.vending\"")
            replaceInstruction(9, "nop")
        }
    }
}
