package app.template.patches.instantmail

import app.morphe.patcher.patch.rawResourcePatch

/**
 * Treats every mail service as free in the app's local service-domain check.
 *
 * Instant Mail 8.0.0 decides whether a mail service tab is premium by checking
 * the selected domain against a local premium-domain list in Hermes bytecode.
 * This patch forces that check to return false, so locally gated services are
 * no longer marked premium.
 *
 * This does not create or authorize a premium account. Remote mail services
 * may still reject address creation or delivery.
 */
@Suppress("unused")
val unlockInstantMailPremiumMailServicesPatch = rawResourcePatch(
    name = "Unlock Instant Mail Premium Mail Services",
    description = "Treats locally gated premium mail services as free. Remote service authorization is unchanged.",
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
                        offset = 4_233_723,
                        expected = byteArrayOf(0x0B, 0x00, 0x00),
                        replacement = byteArrayOf(0x79, 0x00, 0x00),
                    ),
                ),
            ),
        )
    }
}
