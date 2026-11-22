package app.template.patches.disable_test_ads

import app.morphe.patcher.Fingerprint

object TargetFingerprint : Fingerprint(
    definingClass = "Ltests/androguard/TestActivity;",
    name = "onCreate",
    returnType = "V",
    parameters = listOf("Landroid/os/Bundle;"),
)
