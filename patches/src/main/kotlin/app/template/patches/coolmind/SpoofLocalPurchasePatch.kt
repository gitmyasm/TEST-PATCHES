package app.template.patches.coolmind

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch

/**
 * Reports a synthetic active subscription to the local in-app billing bridge.
 *
 * The app's Dart/AOT code is not modified. This only makes the Android billing
 * plugin return a local purchase record for the premium yearly product, so
 * local UI checks that read `queryPurchasesAsync` can treat the app as owned.
 * Server-side subscription validation, AI quotas, storage quotas, member
 * limits and account entitlement state are not changed.
 */
@Suppress("unused")
val spoofCoolMindLocalPurchasePatch = bytecodePatch(
    name = "Spoof Cool Mind Local Purchase",
    description = "Reports a synthetic active premium purchase to the local billing bridge.",
) {
    compatibleWith(COMPATIBILITY_COOL_MIND)

    execute {
        CoolMindQueryPurchasesFingerprint.method.addInstructions(
            0,
            """
                new-instance v0, Lko;
                invoke-direct {v0}, Lko;-><init>()V
                const/4 v1, 0x0
                iput v1, v0, Lko;->a:I
                iput v1, v0, Lko;->b:I
                const-string v2, ""
                iput-object v2, v0, Lko;->c:Ljava/lang/String;

                new-instance v3, Lorg/json/JSONObject;
                invoke-direct {v3}, Lorg/json/JSONObject;-><init>()V

                const-string v4, "orderId"
                const-string v5, "coolmind.local.premium"
                invoke-virtual {v3, v4, v5}, Lorg/json/JSONObject;->put(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;

                const-string v4, "packageName"
                const-string v5, "mindmap.mind.cool"
                invoke-virtual {v3, v4, v5}, Lorg/json/JSONObject;->put(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;

                const-string v4, "purchaseToken"
                const-string v5, "coolmind.local.premium.token"
                invoke-virtual {v3, v4, v5}, Lorg/json/JSONObject;->put(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;

                const-string v4, "productId"
                const-string v5, "cool_mind_premium_yearly"
                invoke-virtual {v3, v4, v5}, Lorg/json/JSONObject;->put(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;

                const-string v4, "purchaseState"
                const/4 v5, 0x1
                invoke-static {v5}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;
                move-result-object v5
                invoke-virtual {v3, v4, v5}, Lorg/json/JSONObject;->put(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;

                const-string v4, "autoRenewing"
                const/4 v5, 0x1
                invoke-static {v5}, Ljava/lang/Boolean;->valueOf(Z)Ljava/lang/Boolean;
                move-result-object v5
                invoke-virtual {v3, v4, v5}, Lorg/json/JSONObject;->put(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;

                const-string v4, "acknowledged"
                const/4 v5, 0x1
                invoke-static {v5}, Ljava/lang/Boolean;->valueOf(Z)Ljava/lang/Boolean;
                move-result-object v5
                invoke-virtual {v3, v4, v5}, Lorg/json/JSONObject;->put(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;

                const-string v4, "purchaseTime"
                const-wide v12, 0x3bb2cc3d800L
                invoke-static {v12, v13}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;
                move-result-object v5
                invoke-virtual {v3, v4, v5}, Lorg/json/JSONObject;->put(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;

                invoke-virtual {v3}, Lorg/json/JSONObject;->toString()Ljava/lang/String;
                move-result-object v6
                const-string v7, ""
                new-instance v8, Lcom/android/billingclient/api/Purchase;
                invoke-direct {v8, v6, v7}, Lcom/android/billingclient/api/Purchase;-><init>(Ljava/lang/String;Ljava/lang/String;)V

                new-instance v9, Ljava/util/ArrayList;
                invoke-direct {v9}, Ljava/util/ArrayList;-><init>()V
                invoke-virtual {v9, v8}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

                iget-object v10, p0, Lmo3;->c:Ljava/lang/Object;
                check-cast v10, Lzi1;
                invoke-virtual {v10, v0, v9}, Lzi1;->g(Lko;Ljava/util/List;)V

                const/4 v0, 0x0
                return-object v0
            """.trimIndent(),
        )
    }
}
