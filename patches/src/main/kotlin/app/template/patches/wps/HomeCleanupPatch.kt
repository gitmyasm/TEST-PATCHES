package app.template.patches.wps

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.MethodReference

@Suppress("unused")
val hideWpsTransferAssistantCardPatch = bytecodePatch(
    name = "Hide WPS Transfer Assistant Card",
    description = "Prevents the local Transfer Assistant row from being added to the device home list.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_WPS_OFFICE)

    execute {
        WpsTransferHelperAvailabilityFingerprint.method.replaceWithCleanBody(
            owner = WpsTransferHelperAvailabilityFingerprint.classDef,
            registerCount = 1,
            label = "TransferHelperUtils.c",
            smaliBody = """
                const/4 v0, 0x0
                return v0
            """,
        )
    }
}

@Suppress("unused")
val hideWpsHomeSearchKeywordAdPatch = bytecodePatch(
    name = "Hide WPS Home Search Keyword Ad",
    description = "Prevents the local home search-bar keyword promotion from being shown or opened.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_WPS_OFFICE)

    execute {
        WpsHomeSearchWordUsableFingerprint.method.replaceWithCleanBody(
            owner = WpsHomeSearchWordUsableFingerprint.classDef,
            registerCount = 1,
            label = "HomeSearchWordConfig.isUsable",
            smaliBody = """
                const/4 v0, 0x0
                return v0
            """,
        )
    }
}

@Suppress("unused")
val hideWpsHomeShareTabPatch = bytecodePatch(
    name = "Hide WPS Home Share Tab",
    description = "Skips local registration of the Share tab label and its content page so the Starred tab keeps its own documents.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_WPS_OFFICE)

    execute {
        val method = WpsMainTabBarFingerprint.method
        val instructions = method.implementation?.instructions
        guard(instructions != null, "tab bar method has no body")

        val index = instructions!!.indexOfFirst { instruction ->
            val reference = (instruction as? ReferenceInstruction)?.reference as? MethodReference
            reference?.definingClass == "Lcn/wps/moffice/bridge/MofficeDelegate;" &&
                    reference.name == "isShareFileEnable"
        }
        guard(index >= 0, "share tab registration call not found")
        guard(
            index + 1 < instructions.size && instructions[index + 1].opcode == Opcode.MOVE_RESULT,
            "share tab registration result not found",
        )

        method.replaceInstruction(index, "const/4 v0, 0x0")
        method.replaceInstruction(index + 1, "nop")

        val constructor = WpsRoamingTabControllerConstructorFingerprint.method
        val constructorInstructions = constructor.implementation?.instructions
        guard(constructorInstructions != null, "roaming tab controller constructor has no body")

        val contentIndex = constructorInstructions!!.indexOfFirst { instruction ->
            val reference = (instruction as? ReferenceInstruction)?.reference as? MethodReference
            reference?.definingClass ==
                    "Lcn/wps/moffice/main/local/home/phone/homedataprovider/HomeShareMgr;" &&
                    reference.name == "j"
        }
        guard(contentIndex >= 0, "share tab content registration call not found")
        guard(
            contentIndex + 1 < constructorInstructions.size &&
                    constructorInstructions[contentIndex + 1].opcode == Opcode.MOVE_RESULT,
            "share tab content registration result not found",
        )

        constructor.replaceInstruction(contentIndex, "const/4 v5, 0x0")
        constructor.replaceInstruction(contentIndex + 1, "nop")
    }
}
