package app.template.patches.wps

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.booleanOption
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.proxy.mutableTypes.MutableClass
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod.Companion.toMutable

import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.builder.MutableMethodImplementation
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.MethodReference
import com.android.tools.smali.dexlib2.immutable.ImmutableMethod

internal val bypassPrivilegeInterceptorOption = booleanOption(
    key = "bypassPrivilegeInterceptor",
    default = true,
    title = "Unlock premium tools (main switch)",
    description = "Answers the local VAS interceptor chain with a success response.",
)

internal val unlockPremiumIdentityOption = booleanOption(
    key = "unlockPremiumIdentity",
    default = true,
    title = "Show a Pro account",
    description = "Reports the local Pro flag used by upgrade UI.",
)

internal val unlockMemberLevelOption = booleanOption(
    key = "unlockMemberLevel",
    default = true,
    title = "Open Pro menus",
    description = "Reports local premium SKUs and member level to menus.",
)

internal val unlockPrivilegeChecksOption = booleanOption(
    key = "unlockPrivilegeChecks",
    default = true,
    title = "Pass ownership checks",
    description = "Answers local ownership checks as held.",
)

internal val removeAdsOption = booleanOption(
    key = "removeAds",
    default = true,
    title = "Remove ads",
    description = "Reports the local ad-free flag to the ad gate.",
)

internal val hideOwnedPrivilegeDialogOption = booleanOption(
    key = "hideOwnedPrivilegeDialog",
    default = true,
    title = "Hide the \"already a member\" popup",
    description = "Suppresses the locally built already-owned dialog.",
)

internal val bypassAsyncPrivilegeChecksOption = booleanOption(
    key = "bypassAsyncPrivilegeChecks",
    default = true,
    title = "Hide the upgrade banner on tool pages",
    description = "Suppresses the tool-page upsell banner through its local callback.",
)

internal val denyAiPrivilegesOption = booleanOption(
    key = "denyAiPrivileges",
    default = false,
    title = "Turn AI features off",
    description = "Answers local AI privilege ids as not held.",
)

internal val reportAdGateOfflineOption = booleanOption(
    key = "reportAdGateOffline",
    default = false,
    title = "Skip the ad pre-check",
    description = "Reports the local ad gate as off before request handling.",
)

internal val passSignatureCheckOption = booleanOption(
    key = "passSignatureCheck",
    default = false,
    title = "Report the stock signature",
    description = "Reports the local signature result used only by analytics.",
)

@Suppress("unused")
val wpsPremiumPatch = bytecodePatch(
    name = "Unlock premium features",
    description = "Changes local privilege answers so premium tool menus and local UI states are shown. " +
        "No account, purchase, or stored state is modified.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_WPS_OFFICE)

    val bypassPrivilegeInterceptor by bypassPrivilegeInterceptorOption()
    val unlockPremiumIdentity by unlockPremiumIdentityOption()
    val unlockMemberLevel by unlockMemberLevelOption()
    val unlockPrivilegeChecks by unlockPrivilegeChecksOption()
    val removeAds by removeAdsOption()
    val hideOwnedPrivilegeDialog by hideOwnedPrivilegeDialogOption()
    val bypassAsyncPrivilegeChecks by bypassAsyncPrivilegeChecksOption()
    val denyAiPrivileges by denyAiPrivilegesOption()
    val reportAdGateOffline by reportAdGateOfflineOption()
    val passSignatureCheck by passSignatureCheckOption()

    execute {
        
        
        
        
        
        if (unlockPremiumIdentity == true) {
            listOf(
                "EnVasPrivilegeUtil.d" to WpsEnVasPremiumFingerprint,
                "EnVasPrivilegeUtil.f" to WpsEnVasProFingerprint,
                "KEnVasAssembly.isPremiumVipMember" to WpsKEnVasPremiumFingerprint,
                "KEnVasAssembly.isProVipMember" to WpsKEnVasProFingerprint,
                "KEnVasAssembly.hasPremiumVipPrivilege" to WpsKEnVasHasPremiumFingerprint,
            ).forEach { (label, fingerprint) ->
                fingerprint.method.replaceWithCleanBody(
                    owner = fingerprint.classDef,
                    registerCount = 2,
                    label = label,
                    smaliBody = """
                        const/4 v0, 0x1
                        return v0
                    """,
                )
            }
        }

        
        
        
        
        
        
        
        
        if (unlockMemberLevel == true) {
            WpsMemberListFingerprint.method.replaceWithCleanBody(
                owner = WpsMemberListFingerprint.classDef,
                registerCount = 3,
                label = "PrivilegeAssembly.getAllMemberList",
                smaliBody = """
                    new-instance v0, Ljava/util/ArrayList;
                    invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V

                    const-string v1, "vip_pro"
                    invoke-virtual {v0, v1}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

                    const-string v1, "vip_pro_plus"
                    invoke-virtual {v0, v1}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

                    return-object v0
                """,
            )

            WpsMemberLevelFingerprint.method.replaceWithCleanBody(
                owner = WpsMemberLevelFingerprint.classDef,
                registerCount = 2,
                label = "PrivilegeAssembly.getMemberLevel",
                smaliBody = """
                    const-string v0, "vip_pro_plus"
                    return-object v0
                """,
            )

            listOf(
                "PrivilegeAssembly.isBasicVip" to WpsBasicVipFingerprint,
                "PrivilegeAssembly.isSeniorVip" to WpsSeniorVipFingerprint,
                
                
                
            ).forEach { (label, fingerprint) ->
                fingerprint.method.replaceWithCleanBody(
                    owner = fingerprint.classDef,
                    registerCount = 2,
                    label = label,
                    smaliBody = """
                        const/4 v0, 0x1
                        return v0
                    """,
                )
            }
        }

        
        
        
        
        
        
        if (unlockPrivilegeChecks == true) {
            
            
            
            
            
            val aiGuard = denyAiPrivileges == true

            
            val instanceBody = if (aiGuard) """
                    if-eqz p1, :grant

                    const-string v1, "ai"
                    invoke-virtual {p1, v1}, Ljava/lang/String;->startsWith(Ljava/lang/String;)Z
                    move-result v1
                    if-eqz v1, :deny

                    const-string v1, "_ai"
                    invoke-virtual {p1, v1}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z
                    move-result v1
                    if-eqz v1, :deny

                    const-string v1, "aigc"
                    invoke-virtual {p1, v1}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z
                    move-result v1
                    if-eqz v1, :deny

                    :grant
                    const/4 v0, 0x1
                    return v0

                    :deny
                    const/4 v0, 0x0
                    return v0
                """ else """
                    const/4 v0, 0x1
                    return v0
                """

            
            val staticBody = if (aiGuard) """
                    if-eqz p0, :grant

                    const-string v1, "ai"
                    invoke-virtual {p0, v1}, Ljava/lang/String;->startsWith(Ljava/lang/String;)Z
                    move-result v1
                    if-eqz v1, :deny

                    const-string v1, "_ai"
                    invoke-virtual {p0, v1}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z
                    move-result v1
                    if-eqz v1, :deny

                    const-string v1, "aigc"
                    invoke-virtual {p0, v1}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z
                    move-result v1
                    if-eqz v1, :deny

                    :grant
                    const/4 v0, 0x1
                    return v0

                    :deny
                    const/4 v0, 0x0
                    return v0
                """ else """
                    const/4 v0, 0x1
                    return v0
                """

            WpsCheckFromCacheFingerprint.method.replaceWithCleanBody(
                owner = WpsCheckFromCacheFingerprint.classDef,
                registerCount = 4,
                label = "PrivilegeAssembly.checkFromCache",
                smaliBody = instanceBody,
            )

            WpsEnVasPackFingerprint.method.replaceWithCleanBody(
                owner = WpsEnVasPackFingerprint.classDef,
                registerCount = 4,
                label = "EnVasPrivilegeUtil.e",
                smaliBody = staticBody,
            )
        }

        
        
        
        
        
        
        if (removeAds == true) {
            WpsAdPrivilegeFingerprint.method.replaceWithCleanBody(
                owner = WpsAdPrivilegeFingerprint.classDef,
                registerCount = 2,
                label = "MofficeDelegateImpl.isAdPrivilege",
                smaliBody = """
                    const/4 v0, 0x1
                    return v0
                """,
            )
        }

        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        if (bypassAsyncPrivilegeChecks == true) {
            
            
            
            WpsUpgradeTipsBarFingerprint.method.replaceWithCleanBody(
                owner = WpsUpgradeTipsBarFingerprint.classDef,
                registerCount = 3,
                label = "UpgradeOrExpandTipsBarProcessor.e",
                smaliBody = """
                    const/4 v0, 0x0
                    invoke-interface {p2, v0}, Lcn/wps/moffice/common/tooltip/CheckShowCallback;->onResult(Z)V
                    return-void
                """,
            )
        }

        
        
        
        if (passSignatureCheck == true) {
            WpsSignatureUtilsFingerprint.method.replaceWithCleanBody(
                owner = WpsSignatureUtilsFingerprint.classDef,
                registerCount = 2,
                label = "SignatureUtils.a",
                smaliBody = """
                    const/4 v0, 0x1
                    return v0
                """,
            )
        }

        
        
        
        
        
        
        
        
        if (bypassPrivilegeInterceptor == true) {
            val method = WpsPrivilegeInterceptorFingerprint.method
            
            
            
            
            
            val registers = try {
                method.implementation?.registerCount
            } catch (t: Throwable) {
                null
            } ?: (method.parameterTypes.size + (if ((method.accessFlags and 0x8) != 0) 0 else 1) + 8)
            method.replaceWithCleanBody(
                owner = WpsPrivilegeInterceptorFingerprint.classDef,
                registerCount = registers,
                label = "PrivilegeInterceptor.a",
                smaliBody = """
                    iget-object v1, p1, Lcn/wps/moffice/vas/privilege/intercept/InterceptorChain;->a:$WPS_PRIVILEGE_CONTEXT
                    new-instance v0, $WPS_SUCCESS_RESPONSE
                    invoke-direct {v0, v1}, $WPS_SUCCESS_RESPONSE-><init>($WPS_PRIVILEGE_CONTEXT)V
                    return-object v0
                """,
            )
        }

        
        
        
        
        
        
        
        
        if (hideOwnedPrivilegeDialog == true) {
            val method = WpsOwnedPrivilegeDialogFingerprint.method
            val instructions = method.implementation?.instructions
                ?: throw PatchException("owned privilege dialog run() has no body")
            var showIndex = -1
            instructions.forEachIndexed { index, instruction ->
                val reference = (instruction as? ReferenceInstruction)?.reference as? MethodReference
                    ?: return@forEachIndexed
                if (reference.definingClass == "Lcn/wps/moffice/common/beans/CustomDialog;" &&
                    reference.name == "show"
                ) {
                    showIndex = index
                }
            }
            
            
            
            
            if (showIndex < 0) {
                val alreadyNop = instructions.any { it.opcode == Opcode.NOP }
                if (alreadyNop) {
                    println(
                        "Premium: the \"already owned\" dialog show() call is already removed. Skipping.",
                    )
                } else {
                    throw PatchException(
                        "WPS premium patch: CustomDialog.show() not found in the owned-privilege " +
                            "dialog, and no nop is present either, so the method body is unexpected.",
                    )
                }
            } else {
                method.replaceInstruction(showIndex, "nop")
            }
        }

        
        
        
        
        
        
        
        
        if (reportAdGateOffline == true) {
            val method = WpsAdOnlineGateFingerprint.method
            val registers = method.safeRegisterCount()
            method.replaceWithCleanBody(
                owner = WpsAdOnlineGateFingerprint.classDef,
                registerCount = registers,
                label = "ADUtil.l",
                smaliBody = """
                    const/4 v0, 0x1
                    return v0
                """,
            )
        }
    }
}

internal fun guard(ok: Boolean, message: String) {
    if (!ok) throw PatchException("WPS premium patch 26.11.1 structural check failed: $message")
}

internal fun MutableMethod.safeRegisterCount(): Int {
    val existing = try {
        implementation?.registerCount
    } catch (t: Throwable) {
        null
    }
    if (existing != null) return existing
    val isStatic = (accessFlags and 0x8) != 0
    return parameterTypes.size + (if (isStatic) 0 else 1) + 8
}

internal fun MutableMethod.replaceWithCleanBody(
    owner: MutableClass,
    registerCount: Int,
    label: String,
    smaliBody: String,
): MutableMethod {
    guard(
        registerCount > parameterTypes.size,
        "$label register count $registerCount for ${parameterTypes.size} parameters",
    )

    val cleanMethod = ImmutableMethod(
        definingClass,
        name,
        parameters,
        returnType,
        accessFlags,
        annotations,
        hiddenApiRestrictions,
        MutableMethodImplementation(registerCount),
    ).toMutable()

    cleanMethod.addInstructions(0, smaliBody.trimIndent())
    owner.methods.remove(this)
    owner.methods.add(cleanMethod)
    return cleanMethod
}
