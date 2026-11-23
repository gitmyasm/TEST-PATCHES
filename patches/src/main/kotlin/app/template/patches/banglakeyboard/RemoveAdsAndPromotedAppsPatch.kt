package app.template.patches.banglakeyboard

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.packageMetadata
import app.morphe.patcher.patch.patch

private val AD_PERMISSIONS = setOf(
    "android.permission.ACCESS_ADSERVICES_AD_ID",
    "android.permission.ACCESS_ADSERVICES_ATTRIBUTION",
    "android.permission.ACCESS_ADSERVICES_CUSTOM_AUDIENCE",
    "com.google.android.gms.permission.AD_ID",
)

private val PROMOTIONAL_ACTIVITIES = setOf(
    "com.deshkeyboard.promotedtiles.testing.TilesTestingActivity",
    "com.deshkeyboard.promotedtiles.testing.UserDictViewerActivity",
)

/**
 * Removes ad identifiers and promotional app surfaces with one toggle.
 *
 * The manifest change drops ad-ID permissions and promoted-tile test screens.
 * The bytecode change keeps the promoted app row hidden; the layout starts gone,
 * and only the patched controller call makes it visible.
 */
@Suppress("unused")
val removeAdsAndPromotedAppsPatch = patch(
    name = "Remove Ads & Promoted Apps",
    description = "Removes ad identifiers, promotional app screens, and the promoted app row above the keyboard.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_BANGLA_KEYBOARD)

    execute {
        if (packageMetadata.versionName != "17.7.3") {
            throw PatchException(
                "Unsupported Bangla Keyboard version: ${packageMetadata.versionName}",
            )
        }

        editManifest {
            removePermissions(AD_PERMISSIONS)
            removeComponents("activity", PROMOTIONAL_ACTIVITIES)
        }

        // Instruction 67 is the sole setVisibility(VISIBLE) call for the
        // promoted-tiles lazy view.
        PromotedTilesVisibilityFingerprint.method.replaceInstruction(67, "nop")
    }
}
