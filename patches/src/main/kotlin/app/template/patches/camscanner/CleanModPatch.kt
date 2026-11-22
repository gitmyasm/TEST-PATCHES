package app.template.patches.camscanner

import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.deleteResource
import app.morphe.patcher.patch.packageMetadata
import app.morphe.patcher.patch.rawResourcePatch

/**
 * Removes the modder's traces from the supplied CamScanner mod.
 *
 * The modded build already contains the local Premium bypass
 * (AccountPreference.o〇0OOo〇0()Z returns true). This patch does not change that
 * behaviour; it only removes the injected marker strings from the DEX files and
 * the youarefinished signature files so the resulting APK is free of the
 * modder's branding.
 */
@Suppress("unused")
val cleanCamScannerModPatch = rawResourcePatch(
    name = "Clean CamScanner Mod",
    description = "Removes modder markers from the supplied CamScanner 7.29.5 modded APK.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_CAMSCANNER_MOD)

    execute {
        if (packageMetadata.versionName != "7.29.5.2611170000") {
            throw PatchException(
                "Unsupported CamScanner version: ${packageMetadata.versionName}",
            )
        }

        val markers = listOf(
            "Patched by youarefinished " to " ".repeat("Patched by youarefinished ".length),
            "Smob - Mod obfuscation tool v4.6 by Kirlif'" to
                " ".repeat("Smob - Mod obfuscation tool v4.6 by Kirlif'".length),
        )

        var removed = 0
        for (index in 1..16) {
            val path = if (index == 1) "classes.dex" else "classes$index.dex"
            val file = get(path, true)
            val bytes = file.readBytes()
            var changed = false

            for ((marker, replacement) in markers) {
                val needle = marker.toByteArray()
                val blank = replacement.toByteArray()
                while (true) {
                    val position = indexOfBytes(bytes, needle)
                    if (position < 0) break
                    blank.copyInto(bytes, position)
                    removed++
                    changed = true
                }
            }
            if (changed) file.writeBytes(bytes)
        }

        if (removed == 0) {
            throw PatchException("No modder markers found; this is not the expected CamScanner mod.")
        }

        deleteResource("META-INF/YOUAREFINISHED.RSA")
        deleteResource("META-INF/YOUAREFINISHED.SF")
    }
}

private fun indexOfBytes(haystack: ByteArray, needle: ByteArray): Int {
    outer@ for (start in 0..haystack.size - needle.size) {
        for (offset in needle.indices) {
            if (haystack[start + offset] != needle[offset]) continue@outer
        }
        return start
    }
    return -1
}
