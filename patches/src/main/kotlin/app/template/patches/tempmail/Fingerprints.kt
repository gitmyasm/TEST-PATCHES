package app.template.patches.tempmail

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

/** Local Pairip license check entry point. */
object PairipLicenseCheckFingerprint : Fingerprint(
    definingClass = "Lcom/pairip/licensecheck/LicenseClient;",
    name = "checkLicense",
    isStatic = true,
    returnType = "V",
    parameters = listOf("Landroid/content/Context;"),
)

/**
 * Local free/paid gate.
 *
 * `AppUtils.y()` returns true when the stored session id is empty. Callers
 * use that answer for local premium UI decisions and ad availability.
 */
object AppUtilsIsFreeFingerprint : Fingerprint(
    definingClass = "Lcom/tempmail/utils/AppUtils;",
    name = "y",
    isStatic = false,
    returnType = "Z",
    parameters = listOf("Landroid/content/Context;"),
)

/** Central ad manager gate consulted before loading any ad format. */
object AdManagerCanRequestAdsFingerprint : Fingerprint(
    definingClass = "Lcom/tempmail/data/ads/managers/AdManager;",
    name = "f",
    isStatic = false,
    returnType = "Z",
    parameters = emptyList(),
)

/** Starts the Play In-App Update check on activity creation. */
object InAppUpdateStartFingerprint : Fingerprint(
    definingClass = "Lcom/tempmail/ui/lifecycles/InAppUpdateLifecycle;",
    name = "k",
    isStatic = false,
    returnType = "V",
    parameters = emptyList(),
)

/** Local "an update is needed" comparison used before showing update UI. */
object InAppUpdateNeededFingerprint : Fingerprint(
    definingClass = "Lcom/tempmail/ui/lifecycles/InAppUpdateLifecycle;",
    name = "p",
    isStatic = false,
    returnType = "Z",
    parameters = emptyList(),
)
