package app.template.patches.paytm

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.returnEarly

/**
 * Bypasses a local Paytm root-path detection helper.
 *
 * This targets a concrete local file-path root check. It does not cover all
 * possible root/Magisk/emulator/integrity signals used by Paytm or Protectt.
 */
@Suppress("unused")
val bypassRootDetectionPatch = bytecodePatch(
    name = "Bypass Root Detection",
    description = "Bypasses a local Paytm root-path detection check.",
) {
    compatibleWith(COMPATIBILITY_PAYTM)

    execute {
        RootPathCheckFingerprint.method.returnEarly(false)
        RootPathScanFingerprint.method.returnEarly(false)
    }
}
