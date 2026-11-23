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

/** Builds OkHttp certificate pinner from remote SSL pin config. */
object SSLPinningBuildPinnerFingerprint : Fingerprint(
    definingClass = "Lcom/paytm/network/utils/SSLPinningUtils;",
    name = "buildCertificatePinner",
    isStatic = true,
    returnType = "Lokhttp3/CertificatePinner;",
    parameters = listOf("Landroid/content/Context;"),
)

/** Builds Cronet pin map from remote SSL pin config. */
object SSLPinningCronetPinsFingerprint : Fingerprint(
    definingClass = "Lcom/paytm/network/utils/SSLPinningUtils;",
    name = "getPinsForCronetEngine",
    isStatic = false,
    returnType = "Ljava/util/Map;",
    parameters = listOf("Landroid/content/Context;"),
)

/** Local root-path detection helper used by Paytm utilities. */
object RootPathCheckFingerprint : Fingerprint(
    definingClass = "Lp497q7/LNd998w;",
    name = "cn2fu",
    isStatic = true,
    returnType = "Z",
    parameters = listOf("Landroid/content/Context;"),
)

/** Root-path scan implementation. */
object RootPathScanFingerprint : Fingerprint(
    definingClass = "Lp497q7/LNd998w;",
    name = "gbFHS2",
    isStatic = true,
    returnType = "Z",
    parameters = listOf("Landroid/content/Context;", "Lp497q7/W7Cnq5;"),
)
