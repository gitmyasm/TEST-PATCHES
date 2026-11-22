package app.template.patches.camscanner

import app.morphe.patcher.extensions.replaceInstruction
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.classDefForEach
import app.morphe.patcher.patch.deleteResource
import app.morphe.patcher.patch.mutableClassDefBy
import app.morphe.patcher.patch.packageMetadata
import app.morphe.patcher.patch.patch
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.StringReference

/**
 * Removes the modder's traces from the supplied CamScanner mod.
 *
 * The modded build already contains the local Premium bypass
 * (AccountPreference.o〇0OOo〇0()Z returns true). This patch does not change that
 * behaviour; it only removes the injected marker strings and the
 * youarefinished signature files so the resulting APK is free of the modder's
 * branding.
 *
 * The strings are replaced through the bytecode API, which keeps DEX string
 * tables sorted and valid. Editing raw DEX string bytes directly breaks ART's
 * string_ids ordering and makes the app crash on startup.
 */
@Suppress("unused")
val cleanCamScannerModPatch = patch(
    name = "Clean CamScanner Mod",
    description = "Removes modder markers from the supplied CamScanner 7.29.5 modded APK.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_CAMSCANNER_MOD)

    execute {
        if (packageMetadata.versionName != "7.29.5.2611170000") {
            throw PatchException(
                "Unsupported CamScanner version: ${packageMetadata.versionName}",
            )
        }

        val markers = setOf(
            "Patched by youarefinished ",
            "Smob - Mod obfuscation tool v4.6 by Kirlif'",
        )
        var removed = 0

        classDefForEach { classDef ->
            val mutableClass = mutableClassDefBy(classDef)
            classDef.methods.forEach { method ->
                val instructions = method.implementation?.instructions?.toList() ?: return@forEach
                val methodParameters = method.parameters.map { it.type }
                val mutableMethod = mutableClass.methods.first {
                    it.name == method.name && it.parameters.map { p -> p.type } == methodParameters &&
                        it.returnType == method.returnType
                }
                instructions.forEachIndexed { index, instruction ->
                    val opcode = instruction.opcode
                    if (opcode != Opcode.CONST_STRING && opcode != Opcode.CONST_STRING_JUMBO) {
                        return@forEachIndexed
                    }
                    val value = (instruction as ReferenceInstruction)
                        .reference
                        .let { it as? StringReference }
                        ?.string ?: return@forEachIndexed
                    if (value !in markers) return@forEachIndexed

                    val register = (instruction as OneRegisterInstruction).registerA
                    mutableMethod.replaceInstruction(index, "const-string v$register, \"\"")
                    removed++
                }
            }
        }

        if (removed == 0) {
            throw PatchException(
                "No modder markers found; this is not the expected CamScanner mod.",
            )
        }

        deleteResource("META-INF/YOUAREFINISHED.RSA")
        deleteResource("META-INF/YOUAREFINISHED.SF")
    }
}
