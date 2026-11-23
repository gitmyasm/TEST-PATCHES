package app.template.patches.edrawmind

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch

/**
 * Makes the app's local AI-credit state report a large balance.
 *
 * Both the cached preference value and the balance delivered to UI observers
 * are overridden. This bypasses the app's local "not enough credits" checks;
 * the developer's server still decides whether an AI request succeeds and
 * still performs its own accounting.
 */
@Suppress("unused")
val spoofAICreditsPatch = bytecodePatch(
    name = "Spoof AI Credit Balance",
    description = "Reports a large local AI credit balance so local credit checks pass.",
) {
    compatibleWith(COMPATIBILITY_EDRAWMIND)

    execute {
        AICreditBalanceEventFingerprint.method.addInstructions(
            0,
            """
                const v0, 0x3b9ac9ff
                return v0
            """.trimIndent(),
        )

        PreferenceReaderFingerprint.method.addInstructions(
            0,
            """
                const-string v0, "member_dot_count_balance"
                if-eqz p1, :not_ai_balance
                invoke-virtual {p1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z
                move-result v0
                if-eqz v0, :not_ai_balance
                const v0, 0x3b9ac9ff
                invoke-static {v0}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;
                move-result-object v0
                return-object v0
                :not_ai_balance
            """.trimIndent(),
        )
    }
}
