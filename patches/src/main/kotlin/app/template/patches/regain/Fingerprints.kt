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
