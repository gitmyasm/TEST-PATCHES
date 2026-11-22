package app.template.patches.nicemind

import app.morphe.patcher.PatchException
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.patch

/**
 * Unlocks Nice Mind's locally enforced Prime features.
 *
 * Both supplied builds compare two APKs store the Prime entitlement in
 * SharedPreferences ("app_setting") and expose it through a single controller
 * method. The patch replaces that method with code that constructs a lifetime
 * "mind_map_vip" entitlement, writes it through the app's own setter so all
 * in-memory state and broadcasts are updated, and returns it.
 *
 * Server-side account features (cloud sync, account-tied purchases) are not
 * affected because they are decided by Nice Mind's servers.
 */
@Suppress("unused")
val unlockPrimePatch = patch(
    name = "Unlock Prime",
    description = "Unlocks Nice Mind Prime features that are checked locally.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_NICE_MIND)

    execute {
        val (fingerprint, smali) = when (packageMetadata.versionName) {
            "12.7.0" -> PrimeStateV127Fingerprint to
                """
                    const-string v0, "mind_map_vip"
                    const-wide v1, 0x000003bb2cc3d800L
                    new-instance v3, Lxs5;
                    invoke-direct { v3, v0, v1, v2 }, Lxs5;-><init>(Ljava/lang/String;J)V
                    sget-object v4, Lss5;->a:Lss5;
                    invoke-virtual { v4, p0, v3 }, Lss5;->e(Landroid/content/Context;Lxs5;)V
                    return-object v3
                """.trimIndent()

            "12.8.0" -> PrimeStateV128Fingerprint to
                """
                    const-string v0, "mind_map_vip"
                    const-wide v1, 0x000003bb2cc3d800L
                    new-instance v3, Lvt5;
                    invoke-direct { v3, v0, v1, v2 }, Lvt5;-><init>(Ljava/lang/String;J)V
                    sget-object v4, Lqt5;->a:Lqt5;
                    invoke-virtual { v4, p0, v3 }, Lqt5;->e(Landroid/content/Context;Lvt5;)V
                    return-object v3
                """.trimIndent()

            else -> throw PatchException(
                "Unsupported Nice Mind version: ${packageMetadata.versionName}",
            )
        }

        fingerprint.method.addInstructions(0, smali)
    }
}
