package app.template.patches.camscanner

import app.morphe.patcher.patch.patch

private val OFFLINE_PERMISSIONS = setOf(
    "android.permission.INTERNET",
)

/** Removes the permission that all outbound and inbound network access depends on. */
@Suppress("unused")
val offlineModePatch = patch(
    name = "Offline Mode",
    description = "Removes the INTERNET permission so CamScanner cannot open network connections. Local scanning, editing and exports stay available.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_CAMSCANNER)

    execute {
        editManifest {
            removePermissions(OFFLINE_PERMISSIONS)
        }
    }
}
