package app.template.patches.regain

import app.morphe.patcher.Fingerprint

/** App-level local "is Pro user" use case. */
object ProUserUseCaseFingerprint : Fingerprint(
    definingClass = "Lmd/w0;",
    name = "a",
    isStatic = false,
    returnType = "Ljava/lang/Object;",
    parameters = listOf(
        "Lcom/revenuecat/purchases/CacheFetchPolicy;",
        "LBw/f;",
    ),
)

/** Subscription manager local "is Pro user" check. */
object SubscriptionManagerProCheckFingerprint : Fingerprint(
    definingClass = "Ljd/t;",
    name = "f",
    isStatic = false,
    returnType = "Ljava/lang/Object;",
    parameters = listOf(
        "Lcom/revenuecat/purchases/CacheFetchPolicy;",
        "LBw/f;",
    ),
)

/** Higher-level local Pro wrapper used by app UI gates. */
object SubscriptionWrapperFingerprint : Fingerprint(
    definingClass = "Lmd/a;",
    name = "d",
    isStatic = false,
    returnType = "Ljava/lang/Object;",
    parameters = listOf("LDw/c;"),
)

/** In-house ad view-model UI event handler. */
object InHouseAdEventHandlerFingerprint : Fingerprint(
    definingClass = "Lwd/F;",
    name = "a",
    isStatic = false,
    returnType = "V",
    parameters = listOf("Lai/blox100/feature_subscription/presentation/in_house_ad/InHouseAdUiEvent;"),
)

/** In-house ad view-model state handler. */
object InHouseAdStateHandlerFingerprint : Fingerprint(
    definingClass = "Lwd/F;",
    name = "b",
    isStatic = false,
    returnType = "V",
    parameters = listOf("Lm1/c;"),
)
