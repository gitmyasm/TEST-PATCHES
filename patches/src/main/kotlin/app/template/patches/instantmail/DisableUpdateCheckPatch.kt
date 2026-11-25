package app.template.patches.instantmail

import app.morphe.patcher.patch.rawResourcePatch

/**
 * Disables Instant Mail's internal update check.
 *
 * Instant Mail 8.0.0 checks for updates on startup, checks again when the app
 * returns to the foreground, and exposes check/download helpers in Hermes
 * bytecode. This patch forces those local paths to their disabled branches:
 * the startup and foreground effects never call the update helper, and the
 * check/download helpers return without contacting the update service.
 *
 * This only changes client-side update behavior. It does not alter any remote
 * release metadata or server-side availability.
 */
@Suppress("unused")
val disableInstantMailUpdateCheckPatch = rawResourcePatch(
    name = "Disable Instant Mail Update Check",
    description = "Disables the app's internal startup, foreground, and manual update check/download paths.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_INSTANT_MAIL)

    execute {
        val file = get(BUNDLE_PATH, true)
        file.writeBytes(
            patchHbc(
                file.readBytes(),
                listOf(
                    // checkForUpdates: force Updates.isEnabled to false.
                    HbcEdit(
                        offset = 4_850_070,
                        expected = byteArrayOf(
                            0x37, 0x02, 0x02, 0x01, 0xE5.toByte(), 0xB4.toByte(),
                        ),
                        replacement = byteArrayOf(0x79, 0x02, 0x79, 0x02, 0x79, 0x02),
                    ),
                    // downloadAndReload: force Updates.isEnabled to false.
                    HbcEdit(
                        offset = 4_850_528,
                        expected = byteArrayOf(
                            0x37, 0x01, 0x01, 0x01, 0xE5.toByte(), 0xB4.toByte(),
                        ),
                        replacement = byteArrayOf(0x79, 0x01, 0x79, 0x01, 0x79, 0x01),
                    ),
                    // downloadSilently: force Updates.isEnabled to false.
                    HbcEdit(
                        offset = 4_850_739,
                        expected = byteArrayOf(
                            0x37, 0x02, 0x02, 0x01, 0xE5.toByte(), 0xB4.toByte(),
                        ),
                        replacement = byteArrayOf(0x79, 0x02, 0x79, 0x02, 0x79, 0x02),
                    ),
                    // useAppUpdatesOnStart startup effect: force appReady false.
                    HbcEdit(
                        offset = 4_851_276,
                        expected = byteArrayOf(0x2E.toByte(), 0x01, 0x00, 0x00),
                        replacement = byteArrayOf(0x79, 0x01, 0x79, 0x01),
                    ),
                    // useAppUpdatesOnStart foreground effect: force appReady false.
                    HbcEdit(
                        offset = 4_851_321,
                        expected = byteArrayOf(0x2E.toByte(), 0x01, 0x01, 0x00),
                        replacement = byteArrayOf(0x79, 0x01, 0x79, 0x01),
                    ),
                ),
            ),
        )
    }
}
