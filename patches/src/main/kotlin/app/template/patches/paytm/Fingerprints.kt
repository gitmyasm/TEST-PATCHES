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

/** OAuth data provider reports rooted device status to the OAuth module. */
object OAuthRootedDeviceFingerprint : Fingerprint(
    definingClass = "Ldc/MY3jR;",
    name = "isRootedDevice",
    isStatic = false,
    returnType = "Z",
    parameters = emptyList(),
)

/** Local Frida hook detection aggregator. */
object FridaHookDetectionFingerprint : Fingerprint(
    definingClass = "LN5/SFMiYa;",
    name = "eszJ2B0a",
    isStatic = true,
    returnType = "Z",
    parameters = listOf("Ljava/util/List;"),
)

/** Local developer options enabled check. */
object DeveloperOptionsEnabledFingerprint : Fingerprint(
    definingClass = "LN5/VVpVRy;",
    name = "eLbhX59K",
    isStatic = true,
    returnType = "Z",
    parameters = listOf("Landroid/content/Context;"),
)

/** Local USB/ADB/wireless debugging enabled check. */
object UsbDebuggingEnabledFingerprint : Fingerprint(
    definingClass = "LN5/VVpVRy;",
    name = "ivfTYNl",
    isStatic = true,
    returnType = "Z",
    parameters = listOf("Landroid/content/Context;"),
)

/** Local Protectt emulator-detection rule check. */
object EmulatorDetectionRuleFingerprint : Fingerprint(
    definingClass = "LZ9/Ncz6mJ;",
    name = "jwHWK",
    isStatic = true,
    returnType = "V",
    parameters = listOf("Lai/protectt/app/security/shouldnotobfuscated/dto/Rule;"),
)
