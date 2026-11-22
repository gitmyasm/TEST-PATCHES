package app.template.patches.nicemind

import app.morphe.patcher.Fingerprint

/**
 * Reads the locally stored Prime state from SharedPreferences ("app_setting").
 *
 * R8 renamed the controller between app versions, so a separate fingerprint
 * is used for each supported version. The obfuscated model objects are:
 *  - 12.7.0: Lss5;.b() -> Lxs5;
 *  - 12.8.0: Lqt5;.b() -> Lvt5;
 */
object PrimeStateV127Fingerprint : Fingerprint(
    definingClass = "Lss5;",
    name = "b",
    isStatic = true,
    returnType = "Lxs5;",
    parameters = listOf("Landroid/content/ContextWrapper;"),
)

object PrimeStateV128Fingerprint : Fingerprint(
    definingClass = "Lqt5;",
    name = "b",
    isStatic = true,
    returnType = "Lvt5;",
    parameters = listOf("Landroid/content/ContextWrapper;"),
)
