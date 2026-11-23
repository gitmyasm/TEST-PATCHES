package app.template.patches.noji

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch

/**
 * Removes the local dependencies on the Play Store and Google Play services.
 *
 * The package is re-signed during patching, so the embedded signature can never
 * match. The integrity check is therefore returned from on its failure path.
 * Licensing is not started at all, and the local-install gate is forced open so
 * error handling never classifies the installation as unlicensed. Finally, the
 * shutdown and store-dialog launchers are made inert in case another code path
 * reaches them. The device-info accessor still reports the store installer so
 * application-level install-source logic sees a consistent answer.
 *
 * This patch is independent of the premium, ads, and trackers patches.
 */
@Suppress("unused")
val aospCompatibilityPatch = bytecodePatch(
    name = "AOSP Compatibility (No Google Services)",
    description = "Disables Pairip signature, licensing, installer and store-dialog checks so Noji runs on AOSP without Google Play services.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_NOJI)

    execute {
        // The mismatch branch throws at instruction 36. Returning there keeps
        // the success path and all surrounding logging intact.
        PairipSignatureCheckFingerprint.method.apply {
            replaceInstruction(36, "return-void")
        }

        // Instruction 14 executes the licensing runnable. Skipping it means no
        // service binding, no retries and no error dialog is ever scheduled.
        PairipLicenseCheckFingerprint.method.apply {
            replaceInstruction(14, "nop")
        }

        // The false result at instruction 10 is what lets error handling show
        // the store dialog when no licensing service is available.
        PairipLocalCheckPassedFingerprint.method.apply {
            replaceInstruction(10, "const/4 v0, 1")
        }

        // Instruction 1 initializes the installer-check result to false.
        PairipLocalInstallerCheckFingerprint.method.apply {
            replaceInstruction(1, "const/4 v1, 1")
        }

        // Neither launcher should shut the app down or open a store dialog.
        PairipErrorDialogFingerprint.method.apply {
            replaceInstruction(0, "return-void")
            (1..5).forEach { replaceInstruction(it, "nop") }
        }

        PairipPaywallFingerprint.method.apply {
            replaceInstruction(0, "return-void")
            (1..5).forEach { replaceInstruction(it, "nop") }
        }

        // The licensing activity may still be started by another path.
        PairipShowErrorDialogFingerprint.method.apply {
            replaceInstruction(0, "return-void")
            (1..3).forEach { replaceInstruction(it, "nop") }
        }

        PairipShowPaywallFingerprint.method.apply {
            replaceInstruction(0, "return-void")
            (1..13).forEach { replaceInstruction(it, "nop") }
        }

        DeviceInstallerPackageFingerprint.method.apply {
            replaceInstruction(8, "const-string v0, \"com.android.vending\"")
            replaceInstruction(9, "nop")
        }
    }
}
