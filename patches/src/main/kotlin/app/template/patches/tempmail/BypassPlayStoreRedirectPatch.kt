package app.template.patches.tempmail

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.returnEarly

/**
 * Blocks the local license paywall handoff that can open Play Store.
 *
 * Pairip's VM startup is left intact, so transformed app code keeps working.
 * Only the local paywall activity, its forced shutdown, and its error dialog
 * are made inert. Remote license, integrity, and entitlement services are not
 * modified.
 */
@Suppress("unused")
val bypassTempMailPlayStoreRedirectPatch = bytecodePatch(
    name = "Bypass Temp Mail Play Store Redirect",
    description = "Blocks the local Pairip paywall handoff and forced shutdown that can open Play Store.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_TEMP_MAIL)

    execute {
        PairipStartPaywallActivityFingerprint.method.returnEarly()
        PairipScheduleShutdownFingerprint.method.returnEarly()
        PairipStartErrorDialogFingerprint.method.returnEarly()

        // Keep Activity.onStart() valid, but close the license screen before it
        // can send the stored Play Store PendingIntent. Original instructions
        // stay after the early return, so existing labels remain intact.
        PairipLicenseActivityOnStartFingerprint.method.addInstructions(
            0,
            """
            invoke-super {p0}, Landroid/app/Activity;->onStart()V
            invoke-virtual {p0}, Landroid/app/Activity;->finish()V
            return-void
            """.trimIndent(),
        )
    }
}
