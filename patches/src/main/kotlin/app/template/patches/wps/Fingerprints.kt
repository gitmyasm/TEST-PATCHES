package app.template.patches.wps

import app.morphe.patcher.Fingerprint

internal const val WPS_PRIVILEGE_ASSEMBLY =
    "Lcn/wpsx/module/communication/vas/service/impl/PrivilegeAssembly;"

internal const val WPS_ENVAS_PRIVILEGE_UTIL = "Lcn/wps/moffice/privilege/EnVasPrivilegeUtil;"

internal const val WPS_KENVAS_ASSEMBLY = "Lcn/wps/moffice/KEnVasAssembly;"

internal const val WPS_MOFFICE_DELEGATE_IMPL = "Lcn/wps/moffice/MofficeDelegateImpl;"

internal const val WPS_SIGNATURE_UTILS = "Lcn/wps/moffice/firebase/SignatureUtils;"

internal const val WPS_CONSUME_PRIVILEGE =
    "Lcn/wpsx/module/communication/vas/bean/ConsumePrivilege;"

internal const val WPS_SUCCESS_RESPONSE =
    "Lcn/wps/moffice/vas/privilege/request/SuccessResponse;"

internal const val WPS_PRIVILEGE_CONTEXT = "Lcn/wps/moffice/vas/privilege/request/VasContext;"

internal object WpsCheckFromCacheFingerprint : Fingerprint(
    definingClass = WPS_PRIVILEGE_ASSEMBLY,
    name = "checkFromCache",
    returnType = "Z",
    parameters = listOf("Ljava/lang/String;"),
)

internal object WpsMemberListFingerprint : Fingerprint(
    definingClass = WPS_PRIVILEGE_ASSEMBLY,
    name = "getAllMemberList",
    returnType = "Ljava/util/ArrayList;",
    parameters = emptyList(),
)

internal object WpsMemberLevelFingerprint : Fingerprint(
    definingClass = WPS_PRIVILEGE_ASSEMBLY,
    name = "getMemberLevel",
    returnType = "Ljava/lang/String;",
    parameters = emptyList(),
)

internal object WpsBasicVipFingerprint : Fingerprint(
    definingClass = WPS_PRIVILEGE_ASSEMBLY,
    name = "isBasicVip",
    returnType = "Z",
    parameters = emptyList(),
)

internal object WpsSeniorVipFingerprint : Fingerprint(
    definingClass = WPS_PRIVILEGE_ASSEMBLY,
    name = "isSeniorVip",
    returnType = "Z",
    parameters = emptyList(),
)

internal object WpsVipAiFingerprint : Fingerprint(
    definingClass = WPS_PRIVILEGE_ASSEMBLY,
    name = "isVipAi",
    returnType = "Z",
    parameters = emptyList(),
)

internal object WpsEnVasPremiumFingerprint : Fingerprint(
    definingClass = WPS_ENVAS_PRIVILEGE_UTIL,
    name = "d",
    returnType = "Z",
    parameters = emptyList(),
)

internal object WpsEnVasProFingerprint : Fingerprint(
    definingClass = WPS_ENVAS_PRIVILEGE_UTIL,
    name = "f",
    returnType = "Z",
    parameters = emptyList(),
)

internal object WpsEnVasPackFingerprint : Fingerprint(
    definingClass = WPS_ENVAS_PRIVILEGE_UTIL,
    name = "e",
    returnType = "Z",
    parameters = listOf("Ljava/lang/String;"),
)

internal object WpsKEnVasPremiumFingerprint : Fingerprint(
    definingClass = WPS_KENVAS_ASSEMBLY,
    name = "isPremiumVipMember",
    returnType = "Z",
    parameters = emptyList(),
)

internal object WpsKEnVasProFingerprint : Fingerprint(
    definingClass = WPS_KENVAS_ASSEMBLY,
    name = "isProVipMember",
    returnType = "Z",
    parameters = emptyList(),
)

internal object WpsKEnVasHasPremiumFingerprint : Fingerprint(
    definingClass = WPS_KENVAS_ASSEMBLY,
    name = "hasPremiumVipPrivilege",
    returnType = "Z",
    parameters = emptyList(),
)

internal object WpsAdPrivilegeFingerprint : Fingerprint(
    definingClass = WPS_MOFFICE_DELEGATE_IMPL,
    name = "isAdPrivilege",
    returnType = "Z",
    parameters = emptyList(),
)

internal object WpsSignatureUtilsFingerprint : Fingerprint(
    definingClass = WPS_SIGNATURE_UTILS,
    name = "a",
    returnType = "Z",
    parameters = listOf("Landroid/content/Context;"),
)

internal object WpsPrivilegeInterceptorFingerprint : Fingerprint(
    definingClass = "Lcn/wps/moffice/vas/privilege/biz/interceptor/PrivilegeInterceptor;",
    name = "a",
    returnType = "Ljava/lang/Object;",
    parameters = listOf(
        "Lcn/wps/moffice/vas/privilege/intercept/InterceptorChain;",
        "Lkotlin/coroutines/Continuation;",
    ),
)

internal object WpsOwnedPrivilegeDialogFingerprint : Fingerprint(
    definingClass = "Ls74;",
    name = "run",
    returnType = "V",
    parameters = emptyList(),
)

internal object WpsAdOnlineGateFingerprint : Fingerprint(
    definingClass = "Lcn/wps/moffice/common/ADUtil;",
    name = "l",
    returnType = "Z",
    parameters = emptyList(),
)

internal const val WPS_UPGRADE_TIPS_BAR_PROCESSOR =
    "Lcn/wps/moffice/common/upgradetipsbar/UpgradeOrExpandTipsBarProcessor;"

internal object WpsUpgradeTipsBarFingerprint : Fingerprint(
    definingClass = WPS_UPGRADE_TIPS_BAR_PROCESSOR,
    name = "e",
    returnType = "V",
    parameters = listOf(
        "Landroid/os/Bundle;",
        "Lcn/wps/moffice/common/tooltip/CheckShowCallback;",
    ),
)

internal const val WPS_HOME_TOP_SHORTCUT_UTIL =
    "Lcn/wps/moffice/main/local/home/phone/v2/hometopshortcut/HomeTopShortcutUtil;"

internal object WpsBigShortcutListFingerprint : Fingerprint(
    definingClass = WPS_HOME_TOP_SHORTCUT_UTIL,
    name = "b",
    returnType = "Ljava/util/List;",
    parameters = listOf("Landroid/app/Activity;"),
)

internal object WpsSmallShortcutListFingerprint : Fingerprint(
    definingClass = WPS_HOME_TOP_SHORTCUT_UTIL,
    name = "d",
    returnType = "Ljava/util/List;",
    parameters = listOf("Landroid/app/Activity;"),
)

internal const val WPS_TRANSFER_HELPER_UTILS =
    "Lcn/wps/moffice/transfer/TransferHelperUtils;"

internal object WpsTransferHelperAvailabilityFingerprint : Fingerprint(
    definingClass = WPS_TRANSFER_HELPER_UTILS,
    name = "c",
    returnType = "Z",
    parameters = emptyList(),
)

internal const val WPS_MAIN_FRAGMENT_TAB_BAR_MANAGER =
    "Lcn/wps/moffice/main/cloud/roaming/phone/controller/PhoneMainFragmentTabBarManager;"

internal object WpsMainTabBarFingerprint : Fingerprint(
    definingClass = WPS_MAIN_FRAGMENT_TAB_BAR_MANAGER,
    name = "e",
    returnType = "V",
    parameters = emptyList(),
)
