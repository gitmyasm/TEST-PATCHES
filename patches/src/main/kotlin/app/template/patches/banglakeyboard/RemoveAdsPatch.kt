package app.template.patches.banglakeyboard

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

/** Removes advertising identifiers and promotional app-suggestion screens. */
@Suppress("unused")
val removeAdsPatch = patch(
    name = "Remove Ads",
    description = "Removes ad identifiers and promotional app-suggestion screens from Bangla Keyboard.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_BANGLA_KEYBOARD)

    execute {
        editManifest {
            removePermissions(AD_PERMISSIONS)
            removeComponents("activity", PROMOTIONAL_ACTIVITIES)
        }
    }
}
