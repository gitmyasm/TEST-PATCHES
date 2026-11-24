package app.template.patches.coolmind

import app.morphe.patcher.Fingerprint

/** Local Pairip license check entry point. */
object CoolMindLicenseCheckFingerprint : Fingerprint(
    definingClass = "Lcom/pairip/licensecheck/LicenseClient;",
    name = "checkLicense",
    isStatic = true,
    returnType = "V",
    parameters = listOf("Landroid/content/Context;"),
)

/** Local in-app billing purchase-query callback implementation. */
object CoolMindQueryPurchasesFingerprint : Fingerprint(
    definingClass = "Lmo3;",
    name = "a",
    isStatic = false,
    returnType = "Ljava/lang/Object;",
    parameters = emptyList(),
)
