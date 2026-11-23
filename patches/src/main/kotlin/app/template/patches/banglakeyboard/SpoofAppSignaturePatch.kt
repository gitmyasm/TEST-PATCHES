package app.template.patches.banglakeyboard

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21c
import com.android.tools.smali.dexlib2.iface.reference.StringReference
import java.util.Base64

private const val HOOK_CLASS = "Lapp/template/extension/extension/SignatureHookApplication;"
private const val PACKAGE_PLACEHOLDER = "PACKAGE_NAME_PLACEHOLDER"
private const val SIGNATURE_PLACEHOLDER = "SIGNATURE_PLACEHOLDER"

/** Static initializer of the injected hook, holding the two constants replaced at patch time. */
internal val SignatureHookApplicationFingerprint = Fingerprint(
    accessFlags = listOf(AccessFlags.STATIC, AccessFlags.CONSTRUCTOR),
    parameters = emptyList(),
    custom = { method, classDef ->
        classDef.type == HOOK_CLASS &&
            method.implementation?.instructions
                ?.filterIsInstance<Instruction21c>()
                ?.count { instruction ->
                    (instruction.reference as? StringReference)?.string in
                        setOf(PACKAGE_PLACEHOLDER, SIGNATURE_PLACEHOLDER)
                } == 2
    },
)

/**
 * Reports the certificate from the APK being patched after re-signing.
 *
 * The certificate is read from the APK being patched and written into a startup hook
 * that replaces `PackageInfo.CREATOR`, so in-app signature reads return the source value.
 */
@Suppress("unused")
val spoofAppSignaturePatch = bytecodePatch(
    name = "Spoof app signature",
    description = "Reports the certificate from the APK being patched to the app after re-signing, preserving in-app signature checks.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_BANGLA_KEYBOARD)

    extendWith("extensions/extension.mpe")

    execute {
        val packageName = packageMetadata.packageName
        val certificate = packageMetadata.signingCertificates.values.flatten().firstOrNull()
            ?: throw PatchException("No signing certificate found in the APK being patched.")
        val encodedSignature = Base64.getEncoder().encodeToString(certificate.encoded)

        val hookClass = SignatureHookApplicationFingerprint.originalClassDef
        val staticInit = SignatureHookApplicationFingerprint.method
        val instructions = staticInit.implementation?.instructions?.toList()
            ?: throw PatchException("Hook class has no static initializer.")

        var packageIndex = -1
        var signatureIndex = -1
        instructions.forEachIndexed { index, instruction ->
            val value = (instruction as? Instruction21c)?.reference
                ?.let { it as? StringReference }
                ?.string
            when (value) {
                PACKAGE_PLACEHOLDER -> packageIndex = index
                SIGNATURE_PLACEHOLDER -> signatureIndex = index
            }
        }
        if (packageIndex < 0 || signatureIndex < 0) {
            throw PatchException("Signature hook placeholders not found.")
        }

        staticInit.replaceInstruction(packageIndex, "const-string v0, \"$packageName\"")
        staticInit.replaceInstruction(signatureIndex, "const-string v1, \"$encodedSignature\"")

        var hooked = 0
        classDefForEach { classDef ->
            if (classDef != hookClass && classDef.superclass == "Landroid/app/Application;") {
                mutableClassDefBy(classDef).setSuperClass(HOOK_CLASS)
                hooked++
            }
        }
        if (hooked == 0) throw PatchException("No Application subclass found to hook.")
    }
}
