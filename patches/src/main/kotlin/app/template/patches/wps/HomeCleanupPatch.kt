package app.template.patches.wps

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.mutableClassDefBy
import app.morphe.patcher.util.proxy.mutableTypes.MutableClass
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.NarrowLiteralInstruction
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.MethodReference
import com.android.tools.smali.dexlib2.iface.reference.StringReference
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

private fun MutableClass.replaceNamedMethod(
    name: String,
    registerCount: Int,
    label: String,
    smaliBody: String,
) {
    val candidates = methods.filter { it.name == name }
    val method = candidates.firstOrNull { it.parameters.isEmpty() } ?: candidates.firstOrNull()
    guard(method != null, "$label method not found")

    method!!.replaceWithCleanBody(
        owner = this,
        registerCount = registerCount,
        label = label,
        smaliBody = smaliBody,
    )
}

private fun MutableMethod.skipBottomToolsTab(label: String) {
    val instructions = implementation?.instructions
    guard(instructions != null, "$label has no body")

    val kitsIndex = instructions!!.indexOfFirst { instruction ->
        val reference = (instruction as? ReferenceInstruction)?.reference
        reference is StringReference && reference.string == "kits"
    }
    guard(kitsIndex >= 0, "$label item tag not found")

    val addIndex = (kitsIndex + 1 until instructions.size).firstOrNull { index ->
        val reference = (instructions[index] as? ReferenceInstruction)?.reference as? MethodReference
        reference?.definingClass == "Ljava/util/ArrayList;" && reference.name == "add"
    }
    guard(addIndex != null, "$label add call not found")

    replaceInstruction(addIndex!!, "nop")
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
val hideWpsRootedDeviceWarningPatch = bytecodePatch(
    name = "Hide WPS Rooted Device Warning",
    description = "Prevents the local rooted-device toast shown every time WPS Office launches.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_WPS_OFFICE)

    execute {
        WpsRootWarningRunnableFingerprint.method.replaceWithCleanBody(
            owner = WpsRootWarningRunnableFingerprint.classDef,
            registerCount = 1,
            label = "HomeControllerImpl rooted-device warning",
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
    description = "Reuses the bottom Discover tab icon as Tools and opens the original Tools page instead of Discover.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_WPS_OFFICE)

    execute {
        val appsPageView = "Lcn/wps/moffice/main/local/home/phone/applicationv2/HomeAppsView;"
        val padAppsPageView = "Lcn/wps/moffice/main/local/home/phone/applicationv2/PadHomeAppsView;"
        val baseView = "Lcn/wps/moffice/main/framework/BaseView;"
        val accountChangeListener =
            "Lcn/wps/moffice/main/local/home/phone/applicationv2/AccountChangeListener;"

        WpsPageTabManagerConstructorFingerprint.method.replaceDiscoverPageWithToolsPage(
            oldType = "Lcn/wps/moffice/main/discover/ui/AiDiscoverPage;",
            newType = WPS_HOME_APPS_PAGE,
            label = "bottom Tools page",
        )

        val homeAppsPage = mutableClassDefBy(WPS_HOME_APPS_PAGE)
        val viewField = homeAppsPage.fields.firstOrNull {
            it.name == "i" && it.type == appsPageView
        }
        guard(viewField != null, "Tools page view field not found")
        viewField!!.setType(padAppsPageView)

        homeAppsPage.replaceNamedMethod(
            name = "b",
            registerCount = 3,
            label = "Tools page view creation",
            smaliBody = """
                new-instance v0, $padAppsPageView
                invoke-virtual {p0}, Landroid/app/Fragment;->getActivity()Landroid/app/Activity;
                move-result-object v1
                invoke-direct {v0, v1}, $padAppsPageView-><init>(Landroid/app/Activity;)V
                iput-object v0, p0, $WPS_HOME_APPS_PAGE->i:$padAppsPageView
                const-string v1, "\u516c\u5171\u5e94\u7528\u9875"
                invoke-static {v1}, Lcn/wps/moffice/main/local/NodeLink;->create(Ljava/lang/String;)Lcn/wps/moffice/main/local/NodeLink;
                move-result-object v1
                invoke-virtual {v0, v1}, $baseView->setNodeLink(Lcn/wps/moffice/main/local/NodeLink;)V
                invoke-virtual {v0}, $baseView->getMainView()Landroid/view/View;
                move-result-object v0
                const/4 v1, 0x0
                invoke-static {v0, v1}, Lcn/wps/moffice/common/secondfloor/util/SecondFloorUtil;->c(Landroid/view/View;Landroid/content/res/Configuration;)V
                iget-object p0, p0, $WPS_HOME_APPS_PAGE->i:$padAppsPageView
                return-object p0
            """,
        )

        val newGuideSelectActivity =
            "Lcn/wps/moffice/main/local/home/phone/application/NewGuideSelectActivity;"
        val activityResultHelper = homeAppsPage.methods
            .firstOrNull {
                it.name == "onActivityResult" &&
                    it.parameters.toList() == listOf("I", "I", "Landroid/content/Intent;")
            }
            ?.implementation
            ?.instructions
            ?.mapNotNull { (it as? ReferenceInstruction)?.reference as? MethodReference }
            ?.firstOrNull {
                it.definingClass == newGuideSelectActivity && it.returnType == "Z"
            }
            ?.name
        guard(activityResultHelper != null, "Tools page activity-result helper not found")

        homeAppsPage.replaceNamedMethod(
            name = "onActivityResult",
            registerCount = 10,
            label = "Tools page activity result",
            smaliBody = """
                invoke-super {p0, p1, p2, p3}, Landroid/app/Fragment;->onActivityResult(IILandroid/content/Intent;)V
                invoke-virtual {p0}, Landroid/app/Fragment;->getActivity()Landroid/app/Activity;
                move-result-object v0
                iget-object p0, p0, $WPS_HOME_APPS_PAGE->i:$padAppsPageView
                invoke-virtual {p0}, $baseView->getNodeLink()Lcn/wps/moffice/main/local/NodeLink;
                move-result-object v5
                const-string v4, "apps"
                move v1, p1
                move v2, p2
                move-object v3, p3
                invoke-static/range {v0 .. v5}, $newGuideSelectActivity->$activityResultHelper(Landroid/app/Activity;IILandroid/content/Intent;Ljava/lang/String;Lcn/wps/moffice/main/local/NodeLink;)Z
                return-void
            """,
        )

        homeAppsPage.replaceNamedMethod(
            name = "onConfigurationChanged",
            registerCount = 3,
            label = "Tools page configuration change",
            smaliBody = """
                invoke-super {p0, p1}, Landroid/app/Fragment;->onConfigurationChanged(Landroid/content/res/Configuration;)V
                iget-object v0, p0, $WPS_HOME_APPS_PAGE->i:$padAppsPageView
                invoke-virtual {v0}, $baseView->getMainView()Landroid/view/View;
                move-result-object v0
                invoke-static {v0, p1}, Lcn/wps/moffice/common/secondfloor/util/SecondFloorUtil;->c(Landroid/view/View;Landroid/content/res/Configuration;)V
                iget-object p0, p0, $WPS_HOME_APPS_PAGE->i:$padAppsPageView
                invoke-virtual {p0, p1}, $padAppsPageView->onConfigurationChanged(Landroid/content/res/Configuration;)V
                return-void
            """,
        )

        homeAppsPage.replaceNamedMethod(
            name = "onDestroyView",
            registerCount = 3,
            label = "Tools page cleanup",
            smaliBody = """
                invoke-super {p0}, Landroid/app/Fragment;->onDestroyView()V
                iget-object v0, p0, $WPS_HOME_APPS_PAGE->i:$padAppsPageView
                iget-object v0, v0, $padAppsPageView->b:Lcn/wps/moffice/main/local/home/phone/applicationv2/main/adapter/MainAppsV2Adapter;
                if-eqz v0, :cond_0
                iget-object v0, v0, Lcn/wps/moffice/main/local/home/phone/applicationv2/main/adapter/MainAppsV2Adapter;->f:Landroidx/recyclerview/widget/RecyclerView${'$'}RecycledViewPool;
                invoke-virtual {v0}, Landroidx/recyclerview/widget/RecyclerView${'$'}RecycledViewPool;->clear()V
                :cond_0
                sget-object v0, Lcn/wps/moffice/main/local/home/phone/applicationv2/AppService;->k:Lcn/wps/moffice/main/local/home/phone/applicationv2/AppService;
                iget-object v1, p0, $WPS_HOME_APPS_PAGE->i:$padAppsPageView
                iget-object v1, v1, $padAppsPageView->g:Lcn/wps/moffice/main/local/home/phone/applicationv2/presenter/PadAppsPresenter;
                invoke-virtual {v1}, Ljava/lang/Object;->getClass()Ljava/lang/Class;
                invoke-virtual {v0, v1}, Lcn/wps/moffice/main/local/home/phone/applicationv2/AppService;->t(Lcn/wps/moffice/main/local/home/phone/applicationv2/AppService${'$'}AppLoadedListener;)V
                iget-object p0, p0, $WPS_HOME_APPS_PAGE->i:$padAppsPageView
                iget-object p0, p0, $padAppsPageView->h:$accountChangeListener
                invoke-virtual {v0, p0}, Lcn/wps/moffice/main/local/home/phone/applicationv2/AppService;->s($accountChangeListener)V
                return-void
            """,
        )

        homeAppsPage.replaceNamedMethod(
            name = "onResume",
            registerCount = 2,
            label = "Tools page resume",
            smaliBody = """
                invoke-super {p0}, Lcn/wps/moffice/main/local/BasePageFragment;->onResume()V
                invoke-virtual {p0}, Landroid/app/Fragment;->isHidden()Z
                move-result v0
                if-eqz v0, :cond_0
                return-void
                :cond_0
                iget-object p0, p0, $WPS_HOME_APPS_PAGE->i:$padAppsPageView
                invoke-virtual {p0}, $padAppsPageView->onResume()V
                return-void
            """,
        )

        WpsRootActExecutorFingerprint.method.disableBooleanRegistration(
            definingClass = "Lcn/wps/moffice/main/local/home/phone/application/apps/AppSupportUtil;",
            name = "d0",
            resultRegister = "p2",
            label = "sidebar Tools row",
        )

        WpsHomeBottomBarItemsFingerprint.method.replaceResourceId(
            oldId = 0x7f154ba5,
            newId = 0x7f154b8c,
            label = "bottom Discover label",
        )
    }
}

@Suppress("unused")
val hideWpsBottomToolsTabPatch = bytecodePatch(
    name = "Hide WPS Bottom Tools Tab",
    description = "Prevents local registration of the bottom Discover/Tools tab item.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_WPS_OFFICE)

    execute {
        WpsHomeBottomBarItemsFingerprint.method.skipBottomToolsTab("bottom Tools tab")
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
