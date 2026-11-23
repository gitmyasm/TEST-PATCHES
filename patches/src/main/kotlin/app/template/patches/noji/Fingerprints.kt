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

/** Pairip's local check that requires a Play Store installer. */
object PairipLocalInstallerCheckFingerprint : Fingerprint(
    definingClass = "Lcom/pairip/licensecheck/LicenseClient;",
    name = "performLocalInstallerCheck",
    isStatic = true,
    returnType = "Z",
    parameters = emptyList(),
)

/** Device-info accessor used by the app to read the installer package. */
object DeviceInstallerPackageFingerprint : Fingerprint(
    definingClass = "Lcom/learnium/RNDeviceInfo/RNDeviceModule;",
    name = "getInstallerPackageNameSync",
    returnType = "Ljava/lang/String;",
    parameters = emptyList(),
)
