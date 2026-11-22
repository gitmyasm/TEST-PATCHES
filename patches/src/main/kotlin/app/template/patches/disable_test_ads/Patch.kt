package app.template.patches.disable_test_ads

import app.morphe.patcher.patch.patch
import app.morphe.patcher.util.returnEarly

@Suppress("unused")
val disableTestAds = patch(
    name = "Disable Test Ads",
    description = "Generated patch for Test App.",
    default = true
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        TargetFingerprint.method.returnEarly()
    }
}
