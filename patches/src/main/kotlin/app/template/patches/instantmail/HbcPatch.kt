package app.template.patches.instantmail

import app.morphe.patcher.patch.PatchException
import java.security.MessageDigest

internal const val BUNDLE_PATH = "assets/index.android.bundle"
internal const val BUNDLE_SIZE = 4_967_172
private const val FOOTER_SIZE = 20

internal data class HbcEdit(
    val offset: Int,
    val expected: ByteArray,
    val replacement: ByteArray,
)

/**
 * Applies version-specific byte edits to the Hermes bundle and refreshes its
 * trailing SHA-1 footer so the runtime accepts the modified bytecode.
 */
internal fun patchHbc(bytes: ByteArray, edits: List<HbcEdit>): ByteArray {
    if (bytes.size != BUNDLE_SIZE) {
        throw PatchException("Unexpected Instant Mail bundle size: ${bytes.size}")
    }

    val output = bytes.copyOf()
    for (edit in edits) {
        if (edit.offset < 0 || edit.offset + edit.expected.size > output.size - FOOTER_SIZE) {
            throw PatchException("Invalid Instant Mail bundle edit offset: ${edit.offset}")
        }
        val actual = output.copyOfRange(edit.offset, edit.offset + edit.expected.size)
        if (!actual.contentEquals(edit.expected)) {
            throw PatchException(
                "Unexpected Instant Mail bundle bytes at offset ${edit.offset}: " +
                    actual.joinToString(",") { byte -> "0x${byte.toInt().and(0xFF).toString(16)}" },
            )
        }
        edit.replacement.copyInto(output, edit.offset)
    }

    val footerOffset = output.size - FOOTER_SIZE
    val digest = MessageDigest.getInstance("SHA-1").digest(output.copyOfRange(0, footerOffset))
    digest.copyInto(output, footerOffset)
    return output
}
