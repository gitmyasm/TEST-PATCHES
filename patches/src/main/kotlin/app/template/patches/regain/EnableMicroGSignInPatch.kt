package app.template.patches.regain

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch

/**
 * Lets the existing Google sign-in flow run with an alternative Google Play
 * Services provider such as any microG GmsCore variant.
 *
 * Credential Manager disables its Google sign-in provider when its local
 * Google Play Services availability check returns a non-zero code. This patch
 * makes that local check report success, so the normal sign-in UI is used
 * regardless of which provider is installed. It does not issue, forge, or
 * modify any Google account token, and it does not change Regain's backend
 * authentication decision.
 */
@Suppress("unused")
val enableMicroGSignInPatch = bytecodePatch(
    name = "Enable Sign-In With Alternative Google Services Provider",
    description = "Allows Regain's existing Google sign-in flow to run with any microG GmsCore variant. Account tokens and backend authentication are unchanged.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_REGAIN)

    execute {
        CredentialProviderGmsAvailabilityFingerprint.method.addInstructions(
            0,
            """
                const/4 v0, 0x0
                return v0
            """.trimIndent(),
        )
    }
}
