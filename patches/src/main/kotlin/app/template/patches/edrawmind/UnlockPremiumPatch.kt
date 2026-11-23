package app.template.patches.edrawmind

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.returnEarly

/**
 * Marks the locally stored EdrawMind membership as active.
 *
 * This only changes checks performed inside the app. Cloud sync, account
 * validation and any feature that requires a successful server response are
 * still controlled by the developer's servers.
 */
@Suppress("unused")
val unlockPremiumPatch = bytecodePatch(
    name = "Unlock Premium Membership",
    description = "Marks the local subscription, lifetime and cross-platform entitlement checks as active.",
) {
    compatibleWith(COMPATIBILITY_EDRAWMIND)

    execute {
        SubscriptionActiveFingerprint.method.returnEarly(true)
        LifetimeMemberFingerprint.method.returnEarly(true)
        AllPlatformsActiveFingerprint.method.returnEarly(true)
        SubscriptionRefuelFingerprint.method.returnEarly(true)
    }
}
