package app.template.patches.edrawmind

import app.morphe.patcher.Fingerprint

/** Central feature-limit gate used by the UI before opening paid features. */
object FeatureLimitCheckFingerprint : Fingerprint(
    definingClass = "Lcom/edrawsoft/mindmaster/biz/subscription/SubscriptionUtils;",
    name = "a",
    isStatic = true,
    returnType = "Z",
    parameters = listOf("I"),
)

/** Local subscription flag read by the account and membership screens. */
object SubscriptionActiveFingerprint : Fingerprint(
    definingClass = "Lcom/edrawsoft/mindmaster/biz/subscription/SubscriptionUtils;",
    name = "r",
    isStatic = true,
    returnType = "Z",
    parameters = emptyList(),
)

/** Cross-platform entitlement expiry check. */
object AllPlatformsActiveFingerprint : Fingerprint(
    definingClass = "Lcom/edrawsoft/mindmaster/biz/subscription/SubscriptionUtils;",
    name = "m",
    isStatic = true,
    returnType = "Z",
    parameters = emptyList(),
)

/** Lifetime membership flag. */
object LifetimeMemberFingerprint : Fingerprint(
    definingClass = "Lcom/edrawsoft/mindmaster/biz/subscription/SubscriptionUtils;",
    name = "n",
    isStatic = true,
    returnType = "Z",
    parameters = emptyList(),
)

/** Refuel / bonus quota flag shown on the membership dialog. */
object SubscriptionRefuelFingerprint : Fingerprint(
    definingClass = "Lcom/edrawsoft/mindmaster/biz/subscription/SubscriptionUtils;",
    name = "p",
    isStatic = true,
    returnType = "Z",
    parameters = emptyList(),
)

/** Renders operation banners in home, community, mine and member screens. */
object OperationBannerRendererFingerprint : Fingerprint(
    definingClass = "Lcom/edrawsoft/mindmaster/biz/operation/OperationHelper;",
    name = "t",
    isStatic = true,
    returnType = "V",
    parameters = listOf(
        "Lcom/edrawsoft/mindmaster/biz/operation/OperationHelper\$IAttachedViewCallback;",
        "Ljava/util/Map;",
    ),
)
