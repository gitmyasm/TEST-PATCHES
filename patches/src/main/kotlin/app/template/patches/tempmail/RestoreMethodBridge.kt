package app.template.patches.tempmail

import java.lang.reflect.Array
import java.lang.reflect.Method

/**
 * Resolves obfuscated method signatures to real [Method] objects locally.
 *
 * The original build stores these methods in static fields at runtime. This
 * bridge lets the patched app restore those fields without running the
 * startup program that can hand off to the local paywall.
 */
object RestoreMethodBridge {
    private val primitives = mapOf(
        "boolean" to java.lang.Boolean.TYPE,
        "byte" to java.lang.Byte.TYPE,
        "char" to java.lang.Character.TYPE,
        "short" to java.lang.Short.TYPE,
        "int" to java.lang.Integer.TYPE,
        "long" to java.lang.Long.TYPE,
        "float" to java.lang.Float.TYPE,
        "double" to java.lang.Double.TYPE,
        "void" to java.lang.Void.TYPE,
    )

    @JvmStatic
    fun get(signature: String): Method? = try {
        val left = signature.substringBefore('(')
        val parameterText = signature.substringAfter('(').substringBefore(')')
        val className = left.substringBeforeLast('.')
        val methodName = left.substringAfterLast('.')
        val parameterTypes = if (parameterText.isBlank()) {
            emptyArray()
        } else {
            parameterText.split(',').map(::classFor).toTypedArray()
        }
        Class.forName(className)
            .getDeclaredMethod(methodName, *parameterTypes)
            .apply { isAccessible = true }
    } catch (_: Throwable) {
        null
    }

    private fun classFor(name: String): Class<*> {
        var type = name.trim()
        var dimensions = 0
        while (type.endsWith("[]")) {
            dimensions++
            type = type.removeSuffix("[]")
        }
        var result = primitives[type] ?: Class.forName(type)
        repeat(dimensions) {
            result = Array.newInstance(result, 0).javaClass
        }
        return result
    }
}
