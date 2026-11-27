package app.template.patches.tempmail

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.returnEarly

/**
 * Makes Temp Mail's local free/paid gate report a paid session.
 *
 * AppUtils.y() checks whether the locally stored session id is empty. Many
 * UI, repository, and ad-availability paths use that result. Only this local
 * answer is changed; server-side subscription state, premium mailboxes and
 * premium API authorization are untouched.
 */
@Suppress("unused")
val unlockTempMailPremiumPatch = bytecodePatch(
    name = "Unlock Temp Mail Premium (Local)",
    description = "Makes Temp Mail's local free/paid check report premium. Server-side subscription state is unchanged.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_TEMP_MAIL)

    execute {
        AppUtilsIsFreeFingerprint.method.returnEarly(false)
    }
}
