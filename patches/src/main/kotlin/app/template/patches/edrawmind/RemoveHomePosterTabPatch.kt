package app.template.patches.edrawmind

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.removeInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.returnEarly
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.StringReference

/**
 * Removes the poster tab from the global home screen.
 *
 * The Flutter fragment for the poster route is no longer added to the main
 * pager and the tab view is hidden.
 */
@Suppress("unused")
val removeHomePosterTabPatch = bytecodePatch(
    name = "Remove Home Poster Tab",
    description = "Removes the poster tab from the home screen.",
) {
    compatibleWith(COMPATIBILITY_EDRAWMIND)

    execute {
        val method = GlobalMainFragmentsFingerprint.method
        val impl = method.implementation!!

        // Find the poster route string and remove the surrounding 6-instruction block:
        // iget-object, const-string "poster", const-string "/poster/productions",
        // invoke-static EDFlutterHelper.e(...), move-result-object, invoke-interface List.add(...)
        val posterStringIndex = impl.instructions.indexOfFirst { insn ->
            val ref = (insn as? ReferenceInstruction)?.reference as? StringReference
            ref?.string == "/poster/productions"
        }
        if (posterStringIndex < 2) error("Poster route string not found in main fragments method")
        method.removeInstructions(posterStringIndex - 2, 6)

        // Keep the device-specific recalculation from restoring old tab indices.
        MainPageIndicesDeviceFingerprint.method.returnEarly()

        // Hide the poster tab view.
        GlobalMainTabSetupFingerprint.method.addInstructions(
            0,
            """
                iget-object v0, p0, Lcom/edrawsoft/mindmaster/view/app_view/main/PhoneGlobalMainImpl;->binding:Lcom/edrawsoft/mindmaster/databinding/ActivityMainPhoneGlobalBinding;
                iget-object v0, v0, Lcom/edrawsoft/mindmaster/databinding/ActivityMainPhoneGlobalBinding;->layoutMainPageContent:Lcom/edrawsoft/mindmaster/databinding/LayoutMainpageContentBinding;
                iget-object v0, v0, Lcom/edrawsoft/mindmaster/databinding/LayoutMainpageContentBinding;->linearTabPoster:Landroidx/constraintlayout/widget/ConstraintLayout;
                const/16 v1, 0x8
                invoke-virtual {v0, v1}, Landroid/view/View;->setVisibility(I)V
            """.trimIndent(),
        )
    }
}
