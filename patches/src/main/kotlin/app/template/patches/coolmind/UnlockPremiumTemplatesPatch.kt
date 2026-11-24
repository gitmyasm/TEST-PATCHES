package app.template.patches.coolmind

import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.rawResourcePatch

/**
 * Clears the local premium flag on Cool Mind's online template entries.
 *
 * Cool Mind 4.0.0 parses catalog entries in the Dart AOT library. Each entry's
 * server-provided `isPrime` value is stored in the template object and used by
 * the local UI to lock premium maps. This patch changes the branch that
 * preserves that value, so every parsed template is treated as a free entry.
 *
 * This does not alter account subscription state, purchase validation, AI or
 * storage quotas, or any server-side entitlement decision. Template downloads
 * still depend on the app's servers.
 */
@Suppress("unused")
val unlockCoolMindPremiumTemplatesPatch = rawResourcePatch(
    name = "Unlock Cool Mind Premium Templates",
    description = "Clears the local premium flag on template catalog entries so premium maps are not locked locally.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_COOL_MIND)

    execute {
        val path = "lib/arm64-v8a/libapp.so"
        val expectedSize = 21_496_720
        val file = get(path, true)
        val bytes = file.readBytes()

        if (bytes.size != expectedSize) {
            throw PatchException("Unexpected Cool Mind libapp.so size: ${bytes.size}")
        }

        // ARM64 `b.ne` at 0xE5E2D0 chooses whether the parsed `isPrime` value
        // is copied into the template object. Make it unconditional so the
        // parser always takes the free-template path.
        val offset = 15_065_808
        val expected = byteArrayOf(0x61, 0x00, 0x00, 0x54)
        val replacement = byteArrayOf(0x04, 0x00, 0x00, 0x14)
        val actual = bytes.copyOfRange(offset, offset + expected.size)

        if (!actual.contentEquals(expected)) {
            throw PatchException(
                "Unexpected Cool Mind libapp.so bytes at offset $offset: ${actual.joinToString(",")}",
            )
        }

        replacement.copyInto(bytes, offset)
        file.writeBytes(bytes)
    }
}
