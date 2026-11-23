package app.template.patches.paytm

import app.morphe.patcher.Fingerprint

/** Pairip signature integrity check entry point. */
object PairipSignatureVerifyFingerprint : Fingerprint(
    definingClass = "Lcom/pairip/SignatureCheck;",
    name = "verifyIntegrity",
    isStatic = true,
    returnType = "V",
    parameters = listOf("Landroid/content/Context;"),
)

/** Pairip signature comparison helper. */
object PairipSignatureMatchesFingerprint : Fingerprint(
    definingClass = "Lcom/pairip/SignatureCheck;",
    name = "verifySignatureMatches",
    isStatic = true,
    returnType = "Z",
    parameters = listOf("Ljava/lang/String;"),
)
