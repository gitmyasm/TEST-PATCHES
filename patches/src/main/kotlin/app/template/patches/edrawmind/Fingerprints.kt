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

/** Returns the locally cached AI credit balance. */
object AICreditBalanceEventFingerprint : Fingerprint(
    definingClass = "Lcom/edrawsoft/mindmaster/view/app_view/member/presenter/GetAIDotPresenter\$AIDotEvent;",
    name = "d",
    isStatic = false,
    returnType = "I",
    parameters = emptyList(),
)

/** Generic preference reader; used here to intercept the cached AI balance key. */
object PreferenceReaderFingerprint : Fingerprint(
    definingClass = "Lcom/edrawsoft/util/SharedPreferenceUtil;",
    name = "e",
    isStatic = true,
    returnType = "Ljava/lang/Object;",
    parameters = listOf(
        "Landroid/content/Context;",
        "Ljava/lang/String;",
        "Ljava/lang/Object;",
    ),
)

/** Entry point that decides whether the rating dialog is shown. */
object RatingDialogTriggerFingerprint : Fingerprint(
    definingClass = "Lcom/edrawsoft/mindmaster/view/app_view/dialog/ScoreDialog;",
    name = "Z",
    isStatic = true,
    returnType = "V",
    parameters = listOf("Lcom/edrawsoft/mindmaster/view/base/EDBaseActivity;"),
)

/** Builds the global main screen fragment list, including the home AI tab. */
object GlobalMainFragmentsFingerprint : Fingerprint(
    definingClass = "Lcom/edrawsoft/mindmaster/view/app_view/main/PhoneGlobalMainImpl;",
    name = "s",
    isStatic = false,
    returnType = "V",
    parameters = emptyList(),
)

/** Initializes the home tab indices. */
object MainPageIndicesFingerprint : Fingerprint(
    definingClass = "Lcom/edrawsoft/mindmaster/view/app_view/main/MainPageConst;",
    name = "<clinit>",
    isStatic = true,
    returnType = "V",
    parameters = emptyList(),
)

/** Sets up navigation tab click listeners in the global main screen. */
object GlobalMainTabSetupFingerprint : Fingerprint(
    definingClass = "Lcom/edrawsoft/mindmaster/view/app_view/main/PhoneGlobalMainImpl;",
    name = "t",
    isStatic = false,
    returnType = "V",
    parameters = emptyList(),
)

/** Special router callback for the AI coin purchase page. */
object AICoinPurchaseRouteFingerprint : Fingerprint(
    definingClass = "Lcom/edrawsoft/mindmaster/biz/route/RouterManager;",
    name = "f",
    isStatic = true,
    returnType = "Z",
    parameters = listOf("Landroid/content/Context;", "Landroid/net/Uri;"),
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
