package app.template.patches.camscanner

import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.deleteResource
import app.morphe.patcher.patch.packageMetadata
import app.morphe.patcher.patch.patch
import app.morphe.patcher.patch.replaceStrings

/**
 * Removes the modder's traces from the supplied CamScanner mod.
 *
 * The modded build already contains the local Premium bypass
 * (AccountPreference.o〇0OOo〇0()Z returns true). This patch does not change that
 * behaviour; it only removes the injected marker strings and the
 * youarefinished signature files so the resulting APK is free of the modder's
 * branding.
 *
 * Strings are replaced through Morphe's bytecode API so the DEX string table
 * stays sorted and valid. Editing raw DEX string bytes directly would break
 * ART's string_ids ordering and make the app crash on startup.
 */
@Suppress("unused")
val cleanCamScannerModPatch = patch(
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

        val removed = replaceStrings(
            "Patched by youarefinished " to "",
            "Smob - Mod obfuscation tool v4.6 by Kirlif'" to "",
        )
        if (removed == 0) {
            throw PatchException(
                "No modder markers found; this is not the expected CamScanner mod.",
            )
        }

        deleteResource("META-INF/YOUAREFINISHED.RSA")
        deleteResource("META-INF/YOUAREFINISHED.SF")
    }
}
