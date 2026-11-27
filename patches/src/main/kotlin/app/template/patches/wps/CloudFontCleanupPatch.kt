package app.template.patches.wps

import app.morphe.patcher.patch.bytecodePatch

@Suppress("unused")
val hideWpsCloudFontsPatch = bytecodePatch(
    name = "Hide WPS Cloud Fonts",
    description = "Removes cloud-font entries, previews, search and missing-font prompts from the local font panels while leaving installed fonts unchanged.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_WPS_OFFICE)

    execute {
        val disabled = """
            const/4 v0, 0x0
            return v0
        """

        listOf(
            WpsCloudFontGridFingerprint to "cloud font grid",
            WpsCloudFontPreviewFingerprint to "cloud font preview",
            WpsCloudFontSearchFingerprint to "cloud font search",
            WpsMissingFontBannerFingerprint to "missing font banner",
            WpsMissingFontOnlineDetailFingerprint to "missing font online detail",
            WpsStartPanelCloudFontFingerprint to "start panel cloud font entry",
            WpsCloudFontLoadingFingerprint to "cloud font list loading",
        ).forEach { (fingerprint, label) ->
            fingerprint.method.replaceWithCleanBody(
                owner = fingerprint.classDef,
                registerCount = 1,
                label = label,
                smaliBody = disabled,
            )
        }
    }
}
