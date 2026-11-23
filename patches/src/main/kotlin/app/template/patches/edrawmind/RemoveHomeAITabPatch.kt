package app.template.patches.edrawmind

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.removeInstructions
import app.morphe.patcher.patch.bytecodePatch

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
        GlobalMainFragmentsFingerprint.method.removeInstructions(65, 6)

        // Shift POSTER_INDEX from 3 to 2 now that the AI tab is gone.
        MainPageIndicesFingerprint.method.addInstructions(
            9,
            """
                const/4 v0, 0x2
                sput v0, Lcom/edrawsoft/mindmaster/view/app_view/main/MainPageConst;->POSTER_INDEX:I
            """.trimIndent(),
        )

        // Hide the AI tab view itself.
        GlobalMainTabSetupFingerprint.method.addInstructions(
            0,
            """
                iget-object v0, p0, Lcom/edrawsoft/mindmaster/view/app_view/main/PhoneGlobalMainImpl;->binding:Lcom/edrawsoft/mindmaster/databinding/ActivityMainPhoneGlobalBinding;
                iget-object v0, v0, Lcom/edrawsoft/mindmaster/databinding/ActivityMainPhoneGlobalBinding;->layoutMainPageContent:Lcom/edrawsoft/mindmaster/databinding/LayoutMainpageContentBinding;
                iget-object v0, v0, Lcom/edrawsoft/mindmaster/databinding/LayoutMainpageContentBinding;->linearTabAi:Landroid/widget/LinearLayout;
                const/4 v1, 0x8
                invoke-virtual {v0, v1}, Landroid/view/View;->setVisibility(I)V
            """.trimIndent(),
        )
    }
}
