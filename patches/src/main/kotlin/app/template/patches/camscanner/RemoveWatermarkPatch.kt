package app.template.patches.camscanner

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.returnEarly

@Suppress("unused")
val removeWatermarkPatch = bytecodePatch(
    name = "Remove PDF Watermark",
    description = "Stops the PDF watermark from being drawn into exported and shared PDFs.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_CAMSCANNER)

    execute {
        WatermarkHiddenFingerprint.method.returnEarly(true)
    }
}
