package app.template.patches.regain

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch

/**
 * Skips Regain's local short-onboarding screen at startup.
 *
 * This only changes the app-side screen choice for the current local onboarding
 * step so the short-onboarding page is bypassed. It does not change remote
 * configuration, account state, purchases, or any server-side entitlement.
 */
@Suppress("unused")
val removeStartupOnboardingPatch = bytecodePatch(
    name = "Remove Startup Onboarding Page",
    description = "Skips Regain's local short onboarding screen at startup. Remote config and account state are unchanged.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_REGAIN)

    execute {
        OnboardingNavigationFingerprint.method.addInstructions(
            667,
            "sget-object v12, LO9/q;->s0:LO9/q;",
        )
    }
}
