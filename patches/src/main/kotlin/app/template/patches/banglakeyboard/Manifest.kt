package app.template.patches.banglakeyboard

import app.morphe.patcher.patch.resourceXml
import app.morphe.patcher.xml.ResourceXml
import app.morphe.patcher.xml.XmlElement

private const val ANDROID_NAME = "android:name"
private const val ANDROID_VALUE = "android:value"

/** Edits the AndroidManifest.xml of the APK being patched. */
internal fun editManifest(block: ResourceXml.() -> Unit) {
    resourceXml("AndroidManifest.xml").use { xml -> xml.block() }
}

/** Removes application components with any of [names] from [tag]. */
internal fun ResourceXml.removeComponents(tag: String, names: Set<String>) {
    root.children("application")
        .firstOrNull()
        ?.children(tag)
        ?.filter { it.attribute(ANDROID_NAME) in names }
        ?.forEach(XmlElement::remove)
}

/** Sets application meta-data entries with any of [names] to [value], adding them when absent. */
internal fun ResourceXml.setMetaData(names: Set<String>, value: String) {
    val application = root.children("application").firstOrNull() ?: return

    names.forEach { name ->
        val existing = application.children("meta-data")
            .firstOrNull { it.attribute(ANDROID_NAME) == name }

        if (existing != null) {
            existing.setAttribute(ANDROID_VALUE, value)
        } else {
            application.appendChild("meta-data") {
                setAttribute(ANDROID_NAME, name)
                setAttribute(ANDROID_VALUE, value)
            }
        }
    }
}

/** Removes manifest permissions with any of [names]. */
internal fun ResourceXml.removePermissions(names: Set<String>) {
    root.children("uses-permission")
        .filter { it.attribute(ANDROID_NAME) in names }
        .forEach(XmlElement::remove)
}
