package app.template.patches.edrawmind

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.returnEarly

/**
 * Prevents server-provided operation banners from being rendered locally.
 *
 * The app still receives the data, but no promotional banner, popup or snackbar
 * is shown in the home, community, mine or membership screens.
 */
@Suppress("unused")
val removePromotionalBannersPatch = bytecodePatch(
    name = "Remove Promotional Banners",
    description = "Hides operation banners, popups and snackbars in the main app screens.",
) {
    compatibleWith(COMPATIBILITY_EDRAWMIND)

    execute {
        OperationBannerRendererFingerprint.method.returnEarly()
    }
}
