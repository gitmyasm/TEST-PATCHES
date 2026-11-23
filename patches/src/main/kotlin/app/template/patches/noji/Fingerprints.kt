package app.template.patches.noji

import app.morphe.patcher.Fingerprint

/**
 * Converts a Qonversion entitlement into the map sent to the JavaScript layer.
 *
 * The `active` field is read from [QEntitlement.isActive] at instruction 39 and
 * copied into the returned map immediately afterwards. Only that copied value
 * is replaced, so the rest of the entitlement payload is preserved.
 */
object QonversionEntitlementMapperFingerprint : Fingerprint(
    definingClass = "Lio/qonversion/sandwich/MappersKt;",
    name = "toMap",
    isStatic = true,
    returnType = "Ljava/util/Map;",
    parameters = listOf("Lcom/qonversion/android/sdk/dto/entitlements/QEntitlement;"),
)

/** Pairip integrity check that rejects a re-signed package. */
object PairipSignatureCheckFingerprint : Fingerprint(
    definingClass = "Lcom/pairip/SignatureCheck;",
    name = "verifyIntegrity",
    isStatic = true,
    returnType = "V",
    parameters = listOf("Landroid/content/Context;"),
)

/** Pairip entry point that starts the licensing service and its error UI. */
object PairipLicenseCheckFingerprint : Fingerprint(
    definingClass = "Lcom/pairip/licensecheck/LicenseClient;",
    name = "checkLicense",
    isStatic = true,
    returnType = "V",
    parameters = listOf("Landroid/content/Context;"),
)

/** Gate used by Pairip error handling before showing the store dialog. */
object PairipLocalCheckPassedFingerprint : Fingerprint(
    definingClass = "Lcom/pairip/licensecheck/LicenseClient;",
    name = "isLocalCheckPassed",
    isStatic = true,
    returnType = "Z",
    parameters = emptyList(),
)

/** Pairip's local check that requires a Play Store installer. */
object PairipLocalInstallerCheckFingerprint : Fingerprint(
    definingClass = "Lcom/pairip/licensecheck/LicenseClient;",
    name = "performLocalInstallerCheck",
    isStatic = false,
    returnType = "Z",
    parameters = emptyList(),
)

/** Launcher that schedules shutdown and opens the licensing error dialog. */
object PairipErrorDialogFingerprint : Fingerprint(
    definingClass = "Lcom/pairip/licensecheck/LicenseClient;",
    name = "startErrorDialogActivity",
    isStatic = false,
    returnType = "V",
    parameters = emptyList(),
)

/** Launcher that schedules shutdown and opens the store paywall dialog. */
object PairipPaywallFingerprint : Fingerprint(
    definingClass = "Lcom/pairip/licensecheck/LicenseClient;",
    name = "startPaywallActivity",
    isStatic = false,
    returnType = "V",
    parameters = listOf("Landroid/app/PendingIntent;"),
)

/** Error UI shown by the licensing activity. */
object PairipShowErrorDialogFingerprint : Fingerprint(
    definingClass = "Lcom/pairip/licensecheck/LicenseActivity;",
    name = "showErrorDialog",
    isStatic = false,
    returnType = "V",
    parameters = emptyList(),
)

/** Store paywall UI shown by the licensing activity. */
object PairipShowPaywallFingerprint : Fingerprint(
    definingClass = "Lcom/pairip/licensecheck/LicenseActivity;",
    name = "showPaywallAndCloseApp",
    isStatic = false,
    returnType = "V",
    parameters = emptyList(),
)

/** Device-info accessor used by the app to read the installer package. */
object DeviceInstallerPackageFingerprint : Fingerprint(
    definingClass = "Lcom/learnium/RNDeviceInfo/RNDeviceModule;",
    name = "getInstallerPackageNameSync",
    returnType = "Ljava/lang/String;",
    parameters = emptyList(),
)
