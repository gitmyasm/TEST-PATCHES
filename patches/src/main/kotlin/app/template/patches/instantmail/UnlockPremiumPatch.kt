package app.template.patches.instantmail

import app.morphe.patcher.patch.rawResourcePatch

/**
 * Reports the premium entitlement as active to the app's local purchase gate.
 *
 * Instant Mail 8.0.0 reads its subscription status in Hermes bytecode and uses
 * the result to unlock local premium UI paths. This patch replaces the final
 * entitlement comparison with a constant true result.
 *
 * This only changes client-side gating. Account state, purchase validation,
 * and any server-side premium authorization are unchanged.
 */
@Suppress("unused")
val unlockInstantMailPremiumPatch = rawResourcePatch(
    name = "Unlock Instant Mail Premium",
    description = "Makes the local entitlement check report premium as active. Server-side account state is unchanged.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_INSTANT_MAIL)

    execute {
        val file = get(BUNDLE_PATH, true)
        file.writeBytes(
            patchHbc(
                file.readBytes(),
                listOf(
                    HbcEdit(
                        offset = 4_421_519,
                        expected = byteArrayOf(0x11, 0x01, 0x02, 0x01),
                        replacement = byteArrayOf(0x78, 0x01, 0x00, 0x00),
                    ),
                ),
            ),
        )
    }
}
