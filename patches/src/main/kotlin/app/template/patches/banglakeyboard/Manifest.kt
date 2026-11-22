package app.template.patches.banglakeyboard

import app.morphe.patcher.patch.resourceXml
import app.morphe.patcher.xml.ResourceXml
import app.morphe.patcher.xml.XmlElement

private const val ANDROID_NAME = "android:name"

/** Edits the AndroidManifest.xml of the APK being patched. */
internal fun editManifest(block: ResourceXml.() -> Unit) {
    resourceXml("AndroidManifest.xml").use { xml -> xml.block() }
}

/** Removes application components with any of [names] from [tag]. */
internal fun ResourceXml.removeComponents(tag: String, names: Set<String>) {
    root.children(tag)
        .filter { it.attribute(ANDROID_NAME) in names }
        .forEach(XmlElement::remove)
}

/** Removes application meta-data entries with any of [names]. */
internal fun ResourceXml.removeMetaData(names: Set<String>) {
    root.children("application")
        .firstOrNull()
        ?.children("meta-data")
        ?.filter { it.attribute(ANDROID_NAME) in names }
        ?.forEach(XmlElement::remove)
}

/** Removes manifest permissions with any of [names]. */
internal fun ResourceXml.removePermissions(names: Set<String>) {
    root.children("uses-permission")
        .filter { it.attribute(ANDROID_NAME) in names }
        .forEach(XmlElement::remove)
}
