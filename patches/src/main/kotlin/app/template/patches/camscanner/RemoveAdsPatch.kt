package app.template.patches.camscanner

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.returnEarly

@Suppress("unused")
val removeAdsPatch = bytecodePatch(
    name = "Remove Ads",
    description = "Blocks ad requests at the interceptor choke point. Ad SDKs stay bundled but receive no requests.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_CAMSCANNER)

    execute {
        AppInterceptorFingerprint.method.returnEarly(true)
        PositionInterceptorFingerprint.method.returnEarly(true)
        ItemHolderInterceptorFingerprint.method.returnEarly(true)
        SourceInterceptorFingerprint.method.returnEarly(true)
    }
}
