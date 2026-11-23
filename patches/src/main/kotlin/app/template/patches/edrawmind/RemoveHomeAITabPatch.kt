package app.template.patches.edrawmind

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.removeInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.returnEarly
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.StringReference

/**
 * Removes the server-backed Edraw AI tab from the global home screen.
 *
 * The Flutter fragment for the AI home route is no longer added to the main
 * pager, the tab view is hidden, and the poster tab takes its place so the
 * remaining tabs still navigate correctly.
 */
@Suppress("unused")
val removeHomeAITabPatch = bytecodePatch(
    name = "Remove Home AI Tab",
    description = "Removes the server-backed Edraw AI tab from the home screen.",
) {
    compatibleWith(COMPATIBILITY_EDRAWMIND)

    execute {
        // Remove the block that creates and adds the /edrawAi/aiHome fragment.
        val method = GlobalMainFragmentsFingerprint.method
        val impl = method.implementation!!
        val aiRouteIndex = impl.instructions.indexOfFirst { insn ->
            val ref = (insn as? ReferenceInstruction)?.reference as? StringReference
            ref?.string == "/edrawAi/aiHome"
        }
        if (aiRouteIndex < 2) error("AI route string not found in main fragments method")
        method.removeInstructions(aiRouteIndex - 2, 6)

        // Shift POSTER_INDEX from 3 to 2 now that the AI tab is gone.
        MainPageIndicesFingerprint.method.addInstructions(
            9,
            """
                const/4 v0, 0x2
                sput v0, Lcom/edrawsoft/mindmaster/view/app_view/main/MainPageConst;->POSTER_INDEX:I
            """.trimIndent(),
        )
        // Keep the device-specific recalculation from restoring the old slot.
        MainPageIndicesDeviceFingerprint.method.returnEarly()

        // Hide the AI tab view itself.
        GlobalMainTabSetupFingerprint.method.addInstructions(
            0,
            """
                iget-object v0, p0, Lcom/edrawsoft/mindmaster/view/app_view/main/PhoneGlobalMainImpl;->binding:Lcom/edrawsoft/mindmaster/databinding/ActivityMainPhoneGlobalBinding;
                iget-object v0, v0, Lcom/edrawsoft/mindmaster/databinding/ActivityMainPhoneGlobalBinding;->layoutMainPageContent:Lcom/edrawsoft/mindmaster/databinding/LayoutMainpageContentBinding;
                iget-object v0, v0, Lcom/edrawsoft/mindmaster/databinding/LayoutMainpageContentBinding;->linearTabAi:Landroid/widget/LinearLayout;
                const/16 v1, 0x8
                invoke-virtual {v0, v1}, Landroid/view/View;->setVisibility(I)V
            """.trimIndent(),
        )
    }
}
