package app.template.patches.camscanner

import app.morphe.patcher.Fingerprint

internal object AppInterceptorFingerprint : Fingerprint(
    definingClass = "Lcom/intsig/advertisement/interfaces/interceptor/AppInterceptor;",
    returnType = "Z",
    parameters = listOf("Landroid/content/Context;", "Ljava/lang/Object;", "Ljava/lang/Object;"),
)

internal object PositionInterceptorFingerprint : Fingerprint(
    definingClass = "Lcom/intsig/advertisement/interfaces/interceptor/PositionInterceptor;",
    returnType = "Z",
    parameters = listOf("Landroid/content/Context;", "Ljava/lang/Object;", "Ljava/lang/Object;"),
)

internal object ItemHolderInterceptorFingerprint : Fingerprint(
    definingClass = "Lcom/intsig/advertisement/interfaces/interceptor/ItemHolderInterceptor;",
    returnType = "Z",
    parameters = listOf("Landroid/content/Context;", "Ljava/lang/Object;", "Ljava/lang/Object;"),
)

internal object SourceInterceptorFingerprint : Fingerprint(
    definingClass = "Lcom/intsig/advertisement/interfaces/interceptor/SourceInterceptor;",
    returnType = "Z",
    parameters = listOf("Landroid/content/Context;", "Ljava/lang/Object;", "Ljava/lang/Object;"),
)

internal object PirateDialogFingerprint : Fingerprint(
    definingClass = "Lcom/intsig/camscanner/business/PirateAppControl;",
    name = "O8",
    returnType = "Z",
    parameters = listOf("Landroid/content/Context;", "Lcom/intsig/callback/DialogDismissListener;"),
)

internal object PirateToastFingerprint : Fingerprint(
    definingClass = "Lcom/intsig/camscanner/business/PirateAppControl;",
    name = "Oo08",
    returnType = "V",
    parameters = listOf("Landroid/content/Context;"),
)

/** VIP status-code preference reader; value 1 is treated as VIP. */
internal object VipStatusCodeFingerprint : Fingerprint(
    definingClass = "Lcom/intsig/comm/account_data/AccountPreference;",
    returnType = "J",
    strings = listOf("qp3sdjd79xhdas02sd"),
)

/** VIP expiry preference reader; value is epoch seconds and must be in the future. */
internal object VipExpiryFingerprint : Fingerprint(
    definingClass = "Lcom/intsig/comm/account_data/AccountPreference;",
    returnType = "J",
    strings = listOf("tkreds3sdvv22ccsx3xd3"),
)

internal object WatermarkHiddenFingerprint : Fingerprint(
    definingClass = "Lcom/intsig/camscanner/share/rights/ShareRightsUtil;",
    returnType = "Z",
    parameters = listOf("Lkotlin/jvm/functions/Function0;"),
    strings = listOf("checkWatermarkHidden -> watermarkFreeSwitch = "),
)
