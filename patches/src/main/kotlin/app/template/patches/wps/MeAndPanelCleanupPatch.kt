package app.template.patches.wps

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import com.android.tools.smali.dexlib2.iface.reference.MethodReference
import com.android.tools.smali.dexlib2.iface.reference.TypeReference

private const val FEATURE_MANAGER = "Lcn/wps/moffice/main/local/home/me/core/MeFeatureManager;"
private const val FEATURE_CLASS_PREFIX = "Lcn/wps/moffice/main/local/home/me/feature/"

private fun MutableMethod.skipFeatureRegistration(featureName: String) {
    val featureClass = "$FEATURE_CLASS_PREFIX$featureName;"
    val instructions = implementation?.instructions
    guard(instructions != null, "Me feature registration method has no body")

    val creationIndex = instructions!!.indexOfFirst { instruction ->
        instruction.opcode == Opcode.NEW_INSTANCE &&
                (instruction as? ReferenceInstruction)?.reference.let { reference ->
                    (reference as? TypeReference)?.type == featureClass
                }
    }
    guard(creationIndex >= 0, "$featureName creation not found")

    val registrationIndex = (creationIndex + 1 until instructions.size).firstOrNull { index ->
        val reference = (instructions[index] as? ReferenceInstruction)?.reference as? MethodReference
        reference?.definingClass == FEATURE_MANAGER && reference.name == "a"
    }
    guard(registrationIndex != null, "$featureName registration not found")

    replaceInstruction(registrationIndex!!, "nop")
    if (registrationIndex + 1 < instructions.size &&
        instructions[registrationIndex + 1].opcode == Opcode.MOVE_RESULT
    ) {
        replaceInstruction(registrationIndex + 1, "nop")
    }
}

private fun accountPanelRowBody(titles: List<String>): String = buildString {
    appendLine("const/16 v0, 0x8")
    titles.forEachIndexed { index, title ->
        appendLine("const-string v1, \"$title\"")
        appendLine("invoke-virtual {v1, p1}, Ljava/lang/String;->equalsIgnoreCase(Ljava/lang/String;)Z")
        appendLine("move-result v1")
        val nextLabel = if (index == titles.lastIndex) ":show" else ":check_${index + 1}"
        appendLine("if-eqz v1, $nextLabel")
        appendLine("goto :hide")
        if (index != titles.lastIndex) appendLine(":check_${index + 1}")
    }
    appendLine(":hide")
    appendLine("iget-object v1, p0, $WPS_ACCOUNT_PANEL_ROW->j:Landroid/widget/LinearLayout;")
    appendLine("if-eqz v1, :hide_return")
    appendLine("invoke-virtual {v1, v0}, Landroid/view/View;->setVisibility(I)V")
    appendLine(":hide_return")
    appendLine("return-void")
    appendLine(":show")
    appendLine("iget-object v0, p0, $WPS_ACCOUNT_PANEL_ROW->c:Landroid/widget/TextView;")
    appendLine("if-eqz v0, :show_return")
    appendLine("invoke-virtual {v0, p1}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V")
    appendLine("invoke-virtual {p0}, $WPS_ACCOUNT_PANEL_ROW->b()V")
    appendLine(":show_return")
    appendLine("return-void")
}

@Suppress("unused")
val hideWpsMeSupportIconPatch = bytecodePatch(
    name = "Hide WPS Me Support Icon",
    description = "Hides the local help/support icon shown beside the notification bell on the Me page.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_WPS_OFFICE)

    execute {
        WpsUserFeedbackAvailabilityFingerprint.method.replaceWithCleanBody(
            owner = WpsUserFeedbackAvailabilityFingerprint.classDef,
            registerCount = 2,
            label = "UserFeedbackFeature.c",
            smaliBody = """
                const/4 p0, 0x0
                return p0
            """,
        )
    }
}

@Suppress("unused")
val hideWpsMeProUpgradeCardPatch = bytecodePatch(
    name = "Hide WPS Me Pro Upgrade Card",
    description = "Prevents local registration of the membership card shown on the Me page.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_WPS_OFFICE)

    execute {
        WpsMeFeatureRegistrationsFingerprint.method.skipFeatureRegistration("UserVipCardV3Feature")
    }
}

@Suppress("unused")
val hideWpsMePromotionBannerPatch = bytecodePatch(
    name = "Hide WPS Me Promotion Banner",
    description = "Prevents local registration of the promotional banner card shown on the Me page.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_WPS_OFFICE)

    execute {
        WpsMeFeatureRegistrationsFingerprint.method.skipFeatureRegistration("UserPromotionCardFeature")
    }
}

@Suppress("unused")
val hideWpsMeCardAndCouponShortcutsPatch = bytecodePatch(
    name = "Hide WPS Me Card And Coupon Shortcuts",
    description = "Prevents local registration of the card and coupon shortcuts shown on the Me page.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_WPS_OFFICE)

    execute {
        val registrations = WpsMeFeatureRegistrationsFingerprint.method
        registrations.skipFeatureRegistration("UserCouponFeature")
        registrations.skipFeatureRegistration("UserCardBagFeature")
    }
}

@Suppress("unused")
val hideWpsMeOrdersShortcutPatch = bytecodePatch(
    name = "Hide WPS Me Orders Shortcut",
    description = "Prevents local registration of the orders shortcut shown on the Me page.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_WPS_OFFICE)

    execute {
        WpsMeFeatureRegistrationsFingerprint.method.skipFeatureRegistration("UserOrderFeature")
    }
}

@Suppress("unused")
val hideWpsSidebarUpgradeCardPatch = bytecodePatch(
    name = "Hide WPS Sidebar Upgrade Card",
    description = "Hides the local membership upgrade card in the account panel.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_WPS_OFFICE)

    execute {
        WpsAccountPanelRefreshFingerprint.method.replaceWithCleanBody(
            owner = WpsAccountPanelRefreshFingerprint.classDef,
            registerCount = 3,
            label = "AccountPanel.E",
            smaliBody = """
                iget-object v0, p0, $WPS_ACCOUNT_PANEL->k:Landroid/view/View;
                if-eqz v0, :skip_card
                const/16 v1, 0x8
                invoke-virtual {v0, v1}, Landroid/view/View;->setVisibility(I)V
                :skip_card
                iget-object v0, p0, $WPS_ACCOUNT_PANEL->J:Landroid/widget/LinearLayout;
                if-eqz v0, :skip_button
                const/16 v1, 0x8
                invoke-virtual {v0, v1}, Landroid/view/View;->setVisibility(I)V
                :skip_button
                return-void
            """,
        )

        val setup = WpsAccountPanelSetupFingerprint.method
        val instructions = setup.implementation?.instructions
        guard(instructions != null, "account panel setup method has no body")

        val cardAssignment = instructions!!.indexOfFirst { instruction ->
            instruction.opcode == Opcode.IPUT_OBJECT &&
                    ((instruction as? ReferenceInstruction)?.reference as? FieldReference)?.name == "k"
        }
        guard(cardAssignment >= 0, "account panel card view assignment not found")

        setup.addInstructions(
            cardAssignment + 1,
            """
                const/16 v6, 0x8
                invoke-virtual {v3, v6}, Landroid/view/View;->setVisibility(I)V
            """.trimIndent(),
        )
    }
}

@Suppress("unused")
val hideWpsSidebarAccountRowsPatch = bytecodePatch(
    name = "Hide WPS Sidebar Account Rows",
    description = "Hides local account-panel rows titled Account and Security, File Transfer, Feedback and Student Verification.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_WPS_OFFICE)

    execute {
        WpsAccountPanelRowTitleFingerprint.method.replaceWithCleanBody(
            owner = WpsAccountPanelRowTitleFingerprint.classDef,
            registerCount = 4,
            label = "AccountPanelPlaceholderView.f",
            smaliBody = accountPanelRowBody(
                listOf(
                    "Account and Security",
                    "File Transfer",
                    "Feedback",
                    "Student Verification",
                ),
            ),
        )
    }
}
