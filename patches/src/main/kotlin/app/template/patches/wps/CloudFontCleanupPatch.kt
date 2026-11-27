package app.template.patches.wps

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.MethodReference

private const val ONLINE_FONT_CACHE = "Lcn/wps/moffice/fontmanager/internal/OnlineFontCache;"

private fun MutableMethod.emptyRecommendedCloudFonts(label: String) {
    val instructions = implementation?.instructions
    guard(instructions != null, "$label has no body")

    val cacheIndex = instructions!!.indexOfFirst { instruction ->
        instruction.opcode == Opcode.INVOKE_STATIC &&
            (instruction as? ReferenceInstruction)?.reference.let { reference ->
                (reference as? MethodReference)?.definingClass == ONLINE_FONT_CACHE &&
                    reference.name == "d"
            }
    }
    guard(cacheIndex >= 0, "$label cache call not found")
    guard(cacheIndex + 2 < instructions.size, "$label cache call is truncated")

    val resultInstruction = instructions[cacheIndex + 1]
    val listCall = instructions[cacheIndex + 2]
    guard(resultInstruction.opcode == Opcode.MOVE_RESULT_OBJECT, "$label result not found")
    guard(
        listCall is ReferenceInstruction &&
            (listCall.reference as? MethodReference)?.definingClass == ONLINE_FONT_CACHE &&
            (listCall.reference as? MethodReference)?.name == "f",
        "$label list call not found",
    )

    val resultRegister = (resultInstruction as OneRegisterInstruction).registerA
    val sizeRegister = (listCall as FiveRegisterInstruction).registerD
    replaceInstruction(cacheIndex, "new-instance v$resultRegister, Ljava/util/ArrayList;")
    replaceInstruction(cacheIndex + 1, "const/4 v$sizeRegister, 0x0")
    replaceInstruction(
        cacheIndex + 2,
        "invoke-direct {v$resultRegister, v$sizeRegister}, Ljava/util/ArrayList;-><init>(I)V",
    )
}

@Suppress("unused")
val hideWpsCloudFontsPatch = bytecodePatch(
    name = "Hide WPS Cloud Fonts",
    description = "Removes cloud-font entries, previews, search and missing-font prompts from the local font panels while leaving installed fonts unchanged.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_WPS_OFFICE)

    execute {
        WpsFontFlutterBridgeRecFontListFingerprint.method.emptyRecommendedCloudFonts(
            label = "recommended cloud font list",
        )

        val disabled = """
            const/4 v0, 0x0
            return v0
        """

        listOf(
            WpsCloudFontGridFingerprint to "cloud font grid",
            WpsCloudFontPreviewFingerprint to "cloud font preview",
            WpsCloudFontSearchFingerprint to "cloud font search",
            WpsMissingFontBannerFingerprint to "missing font banner",
            WpsMissingFontOnlineDetailFingerprint to "missing font online detail",
            WpsStartPanelCloudFontFingerprint to "start panel cloud font entry",
            WpsCloudFontLoadingFingerprint to "cloud font list loading",
        ).forEach { (fingerprint, label) ->
            fingerprint.method.replaceWithCleanBody(
                owner = fingerprint.classDef,
                registerCount = 1,
                label = label,
                smaliBody = disabled,
            )
        }
    }
}
