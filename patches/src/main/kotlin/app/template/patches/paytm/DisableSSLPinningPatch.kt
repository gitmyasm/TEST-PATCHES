package app.template.patches.paytm

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.returnEarly

/**
 * Disables local OkHttp/Cronet SSL pinning configured by Paytm network utils.
 *
 * This removes local pin enforcement only. Server-side trust, integrity, or
 * attestation checks may still reject modified or intercepted connections.
 */
@Suppress("unused")
val disableSSLPinningPatch = bytecodePatch(
    name = "Disable SSL Pinning",
    description = "Disables local SSL certificate pinning in Paytm network utilities.",
) {
    compatibleWith(COMPATIBILITY_PAYTM)

    execute {
        SSLPinningBuildPinnerFingerprint.method.returnEarly(null)
        SSLPinningCronetPinsFingerprint.method.addInstructions(
            0,
            """
                new-instance v0, Ljava/util/LinkedHashMap;
                invoke-direct {v0}, Ljava/util/LinkedHashMap;-><init>()V
                return-object v0
            """.trimIndent(),
        )
    }
}
