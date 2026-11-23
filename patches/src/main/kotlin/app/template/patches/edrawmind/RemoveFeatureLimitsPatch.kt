package app.template.patches.edrawmind

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.returnEarly

/**
 * Bypasses the central feature-limit gate.
 *
 * The gate covers attachments, OCR, PDF tools, todo/calendar limits, themes,
 * encryption, voice input, history, export formats and other locally counted
 * limits. Some of those features may still need network/account access.
 */
@Suppress("unused")
val removeFeatureLimitsPatch = bytecodePatch(
    name = "Remove Feature Limits",
    description = "Bypasses the local paid-feature limit checks used across the app.",
) {
    compatibleWith(COMPATIBILITY_EDRAWMIND)

    execute {
        FeatureLimitCheckFingerprint.method.returnEarly(true)
    }
}
