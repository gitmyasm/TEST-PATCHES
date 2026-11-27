package app.template.patches.wps

import app.morphe.patcher.patch.bytecodePatch

@Suppress("unused")
val hideWpsNewBlankAiRecommendationsPatch = bytecodePatch(
    name = "Hide WPS New Blank AI Recommendations",
    description = "Prevents the new-blank document and compact AI shortcut bars from loading Activate WPS AI, Scan Input, and Inspiration marketplace.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_WPS_OFFICE)

    execute {
        WpsNewBlankAiConfigFingerprint.method.replaceWithCleanBody(
            owner = WpsNewBlankAiConfigFingerprint.classDef,
            registerCount = 1,
            label = "new blank AI shortcut config",
            smaliBody = """
                const/4 v0, 0x0
                return-object v0
            """,
        )

        WpsCompactPanelRecommendationsFingerprint.method.replaceWithCleanBody(
            owner = WpsCompactPanelRecommendationsFingerprint.classDef,
            registerCount = 3,
            label = "compact AI recommendation bar",
            smaliBody = """
                sget-object v0, Lkotlin/collections/EmptyList;->a:Lkotlin/collections/EmptyList;
                return-object v0
            """,
        )
    }
}
