package app.template.patches.noji

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstructions
import app.morphe.patcher.patch.bytecodePatch

/**
 * Marks the existing Qonversion Premium entitlement as active.
 *
 * Only the copied `active` value in the entitlement map is changed. The rest
 * of the entitlement payload and all account state are left untouched.
 */
@Suppress("unused")
val unlockPremiumPatch = bytecodePatch(
    name = "Unlock Premium",
    description = "Marks the existing Qonversion Premium entitlement as active.",
) {
    compatibleWith(COMPATIBILITY_NOJI)

    execute {
        QonversionEntitlementMapperFingerprint.method.apply {
            // Instructions 39-40 read isActive() and box it; 41 consumes the boxed value.
            replaceInstructions(
                39,
                """
                const/4 v1, 1
                invoke-static {v1}, Ljava/lang/Boolean;->valueOf(Z)Ljava/lang/Boolean;
                """.trimIndent(),
            )
        }
    }
}
