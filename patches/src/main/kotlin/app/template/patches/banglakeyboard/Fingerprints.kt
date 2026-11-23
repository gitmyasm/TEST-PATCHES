package app.template.patches.banglakeyboard

import app.morphe.patcher.Fingerprint

/** Initializes the locally stored premium entitlement stream. */
object BillingEntitlementFingerprint : Fingerprint(
    definingClass = "Lcom/deshkeyboard/billing/e;",
    name = "<init>",
    returnType = "V",
    parameters = listOf(
        "Landroid/content/Context;",
        "Lcom/deshkeyboard/billing/d${'$'}a;",
    ),
)

/** Handles the result delivered to the premium screen. */
object PremiumUiCallbackFingerprint : Fingerprint(
    definingClass = "Lbi/b;",
    name = "b",
    returnType = "V",
    parameters = listOf("Ljava/lang/Object;"),
)

/** Applies promoted-tiles visibility state changes. */
object PromotedTilesVisibilityFingerprint : Fingerprint(
    definingClass = "Lcom/deshkeyboard/promotedtiles/c;",
    name = "C",
    returnType = "V",
    parameters = listOf("Lcom/deshkeyboard/promotedtiles/e;"),
)
