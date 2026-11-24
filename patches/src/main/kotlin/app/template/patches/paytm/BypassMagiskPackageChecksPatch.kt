package app.template.patches.paytm

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.returnEarly

/**
 * Bypasses local Paytm file/path existence checks used in integrity scans.
 *
 * This targets a concrete local helper (U4.SFMiYa.eLbhX59K). It can suppress
 * checks for Magisk/KernelSU/APatch/su paths that go through this helper, but
 * does not cover every package/process/native/kernel detection or any server-side
 * integrity/fraud checks.
 */
@Suppress("unused")
val bypassMagiskPackageChecksPatch = bytecodePatch(
    name = "Bypass Magisk/Root Path Checks",
    description = "Bypasses local Paytm integrity path-existence checks.",
) {
    compatibleWith(COMPATIBILITY_PAYTM)

    execute {
        NativeIntegrityPathExistsFingerprint.method.returnEarly(false)
    }
}
