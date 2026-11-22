package app.template.patches.disable_test_ads

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Test App",
        packageName = "tests.androguard",
        appIconColor = 0xFF0045,
        targets = listOf(
        AppTarget(version = "1.0")
        )
    )
}
