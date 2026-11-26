package app.template.patches.regain

import app.morphe.patcher.Fingerprint

/** Local subscription-state Pro flag getter. */
object SubscriptionStateProFlagFingerprint : Fingerprint(
    definingClass = "Lai/blox100/feature_subscription/domain/model/SubscriptionState;",
    name = "isProUser",
    isStatic = false,
    returnType = "Z",
    parameters = emptyList(),
)

/** Credential Manager's local Google Play Services availability gate. */
object CredentialProviderGmsAvailabilityFingerprint : Fingerprint(
    definingClass = "Landroidx/credentials/playservices/CredentialProviderPlayServicesImpl;",
    name = "isGooglePlayServicesAvailable",
    isStatic = false,
    returnType = "I",
    parameters = listOf("Landroid/content/Context;"),
)
