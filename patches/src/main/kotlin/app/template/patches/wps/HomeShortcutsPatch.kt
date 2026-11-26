package app.template.patches.wps

import app.morphe.patcher.patch.bytecodePatch

@Suppress("unused")
val hideWpsHomeBigShortcutsPatch = bytecodePatch(
    name = "Hide WPS Home Big Shortcuts",
    description = "Returns an empty upper home shortcut list locally, so the top shortcut row is not drawn.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_WPS_OFFICE)

    execute {
        WpsBigShortcutListFingerprint.method.replaceWithCleanBody(
            owner = WpsBigShortcutListFingerprint.classDef,
            registerCount = 3,
            label = "HomeTopShortcutUtil.b",
            smaliBody = """
                new-instance v0, Ljava/util/ArrayList;
                invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V

                return-object v0
            """,
        )
    }
}

@Suppress("unused")
val hideWpsHomeSmallShortcutsPatch = bytecodePatch(
    name = "Hide WPS Home Small Shortcuts",
    description = "Returns an empty lower home shortcut list locally, so the quick-tools row is not drawn.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_WPS_OFFICE)

    execute {
        WpsSmallShortcutListFingerprint.method.replaceWithCleanBody(
            owner = WpsSmallShortcutListFingerprint.classDef,
            registerCount = 3,
            label = "HomeTopShortcutUtil.d",
            smaliBody = """
                new-instance v0, Ljava/util/ArrayList;
                invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V

                return-object v0
            """,
        )
    }
}
