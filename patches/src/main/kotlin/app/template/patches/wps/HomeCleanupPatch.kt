package app.template.patches.wps

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.NarrowLiteralInstruction
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.MethodReference
import com.android.tools.smali.dexlib2.iface.reference.TypeReference

private fun MutableMethod.disableBooleanRegistration(
    definingClass: String,
    name: String,
    resultRegister: String,
    label: String,
) {
    val instructions = implementation?.instructions
    guard(instructions != null, "$label has no body")

    val index = instructions!!.indexOfFirst { instruction ->
        val reference = (instruction as? ReferenceInstruction)?.reference as? MethodReference
        reference?.definingClass == definingClass && reference.name == name
    }
    guard(index >= 0, "$label registration call not found")
    guard(
        index + 1 < instructions.size && instructions[index + 1].opcode == Opcode.MOVE_RESULT,
        "$label registration result not found",
    )

    replaceInstruction(index, "const/4 $resultRegister, 0x0")
    replaceInstruction(index + 1, "nop")
}

private fun MutableMethod.replaceResourceId(oldId: Int, newId: Int, label: String) {
    val instructions = implementation?.instructions
    guard(instructions != null, "$label has no body")

    val index = instructions!!.indexOfFirst { instruction ->
        instruction is NarrowLiteralInstruction && instruction.narrowLiteral == oldId
    }
    guard(index >= 0, "$label resource id not found")

    val instruction = instructions[index]
    guard(instruction is OneRegisterInstruction, "$label register not found")

    replaceInstruction(index, "const v${(instruction as OneRegisterInstruction).registerA}, 0x${newId.toString(16)}")
}

private fun MutableMethod.replaceDiscoverPageWithToolsPage(
    oldType: String,
    newType: String,
    label: String,
) {
    val instructions = implementation?.instructions
    guard(instructions != null, "$label has no body")

    var replacements = 0
    instructions!!.forEachIndexed { index, instruction ->
        val reference = (instruction as? ReferenceInstruction)?.reference
        val referenceType = when (reference) {
            is TypeReference -> reference.type
            is MethodReference -> reference.definingClass
            else -> null
        }
        if (referenceType != oldType) return@forEachIndexed

        val replacement = when (reference) {
            is TypeReference -> "new-instance p1, $newType"
            is MethodReference -> "invoke-direct {p1}, $newType-><init>()V"
            else -> null
        }
        if (replacement != null) {
            replaceInstruction(index, replacement)
            replacements++
        }
    }
    guard(replacements == 2, "$label references not found")
}

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
val hideWpsHomeCashSignPromoPatch = bytecodePatch(
    name = "Hide WPS Home Cash Sign Promo",
    description = "Skips the local home title-bar promo callback so the promo icon is not added or opened.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_WPS_OFFICE)

    execute {
        WpsHomeTitleBarPromoCallbackFingerprint.method.replaceWithCleanBody(
            owner = WpsHomeTitleBarPromoCallbackFingerprint.classDef,
            registerCount = 2,
            label = "HomeViewTitleBar promo callback",
            smaliBody = "return-void",
        )
    }
}

@Suppress("unused")
val hideWpsHomePromoBannersPatch = bytecodePatch(
    name = "Hide WPS Home Promo Banners",
    description = "Skips local home promo banner operators while keeping the home content chain intact.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_WPS_OFFICE)

    execute {
        val proceed = """
            invoke-virtual {p1, p0}, Lcn/wps/moffice/main/local/home/phone/v2/ext/operate/OperateChain;->a(Lcn/wps/moffice/main/local/home/phone/v2/ext/operate/BaseOperator;)V
            return-void
        """

        WpsHomeAdBannerRegisterFingerprint.method.replaceWithCleanBody(
            owner = WpsHomeAdBannerRegisterFingerprint.classDef,
            registerCount = 3,
            label = "HomeAdBanner registration",
            smaliBody = proceed,
        )
        WpsBigNoticeColumnAdRegisterFingerprint.method.replaceWithCleanBody(
            owner = WpsBigNoticeColumnAdRegisterFingerprint.classDef,
            registerCount = 3,
            label = "BigNoticeColumnAd registration",
            smaliBody = proceed,
        )
    }
}

@Suppress("unused")
val hideWpsHomeDeviceTabPatch = bytecodePatch(
    name = "Hide WPS Home This Device Tab",
    description = "Skips local registration of the This Device top tab and its content pages.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_WPS_OFFICE)

    execute {
        val deviceTabAvailability = "Lcn/wps/moffice/main/local/home/device/HomeDeviceMgr;"

        WpsMainTabBarFingerprint.method.disableBooleanRegistration(
            definingClass = deviceTabAvailability,
            name = "isDeviceTabEnable",
            resultRegister = "v0",
            label = "This Device tab label",
        )
        WpsLocalTabControllerConstructorFingerprint.method.disableBooleanRegistration(
            definingClass = deviceTabAvailability,
            name = "isDeviceTabEnable",
            resultRegister = "v6",
            label = "local This Device content",
        )
        WpsRoamingTabControllerConstructorFingerprint.method.disableBooleanRegistration(
            definingClass = deviceTabAvailability,
            name = "isDeviceTabEnable",
            resultRegister = "v5",
            label = "roaming This Device content",
        )
    }
}

@Suppress("unused")
val hideWpsScanTabPatch = bytecodePatch(
    name = "Hide WPS Scan Tab",
    description = "Forces the local bottom-bar scan entry off while keeping the other tabs.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_WPS_OFFICE)

    execute {
        WpsHomeBottomBarItemsFingerprint.method.disableBooleanRegistration(
            definingClass = "Lcn/wps/moffice/common/param/wrapper/KModulePraramsWrapper;",
            name = "a",
            resultRegister = "v1",
            label = "Scan tab registration",
        )
    }
}

@Suppress("unused")
val useWpsToolsAsBottomDiscoverTabPatch = bytecodePatch(
    name = "Use WPS Tools As Bottom Discover Tab",
    description = "Reuses the bottom Discover tab icon as Tools and opens the Tools page instead of Discover.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_WPS_OFFICE)

    execute {
        WpsPageTabManagerConstructorFingerprint.method.replaceDiscoverPageWithToolsPage(
            oldType = "Lcn/wps/moffice/main/discover/ui/AiDiscoverPage;",
            newType = "Lcn/wps/moffice/main/local/home/phone/application/HomeAppsPage;",
            label = "bottom Tools page",
        )
        WpsHomeBottomBarItemsFingerprint.method.replaceResourceId(
            oldId = 0x7f154ba5,
            newId = 0x7f154b8c,
            label = "bottom Discover label",
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
