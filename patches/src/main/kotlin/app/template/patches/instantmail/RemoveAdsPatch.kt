package app.template.patches.instantmail

import app.morphe.patcher.patch.rawResourcePatch

/**
 * Skips the app's interstitial and rewarded ad display calls.
 *
 * Instant Mail 8.0.0 shows ads from two local Hermes bytecode paths: an
 * interstitial display path and a rewarded ad display path. Each path is
 * redirected to its existing early-return branch before the native show call.
 *
 * This suppresses local ad presentation only. It does not change ad network
 * accounts, reward issuance guarantees, or any server-side ad behavior.
 */
@Suppress("unused")
val removeInstantMailAdsPatch = rawResourcePatch(
    name = "Remove Instant Mail Ads",
    description = "Skips interstitial and rewarded ad display calls so ads are not shown locally.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_INSTANT_MAIL)

    execute {
        val file = get(BUNDLE_PATH, true)
        file.writeBytes(
            patchHbc(
                file.readBytes(),
                listOf(
                    // Interstitial ad: jump to the existing false/early-return path.
                    HbcEdit(
                        offset = 4_418_349,
                        expected = byteArrayOf(0x92.toByte(), 0x0A, 0x02),
                        replacement = byteArrayOf(0x8E.toByte(), 0x0A, 0x00),
                    ),
                    // Rewarded ad: jump to the existing not-shown branch.
                    HbcEdit(
                        offset = 4_419_507,
                        expected = byteArrayOf(0xB8.toByte(), 0x1D, 0x03, 0x02),
                        replacement = byteArrayOf(0x8E.toByte(), 0x1D, 0x00, 0x00),
                    ),
                ),
            ),
        )
    }
}
