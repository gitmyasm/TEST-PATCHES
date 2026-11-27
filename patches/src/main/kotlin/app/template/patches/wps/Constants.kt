package app.template.patches.wps

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.SupportedAbi

val COMPATIBILITY_WPS_OFFICE = Compatibility(
    name = "WPS Office",
    packageName = "cn.wps.moffice_eng",
    apkFileType = ApkFileType.APK,
    appIconColor = 0xE64A19,
    targets = listOf(
        AppTarget(
            version = "26.11.1",
            versionCodes = mapOf(
                SupportedAbi.ARM64_V8A to 1651,
                SupportedAbi.ARMEABI_V7A to 1651,
                SupportedAbi.X86_64 to 1651,
                SupportedAbi.X86 to 1651,
            ),
            minSdk = 29,
            description = "Standalone APK, build 1651",
        ),
        AppTarget(
            version = "26.11.2",
            versionCodes = mapOf(
                SupportedAbi.ARM64_V8A to 1653,
                SupportedAbi.ARMEABI_V7A to 1653,
                SupportedAbi.X86_64 to 1653,
                SupportedAbi.X86 to 1653,
            ),
            minSdk = 29,
            description = "Standalone APK, build 1653",
        ),
    ),
)
