package app.template.patches.noji

import app.morphe.patcher.patch.patch

private val AD_PERMISSIONS = setOf(
    "android.permission.ACCESS_ADSERVICES_AD_ID",
    "android.permission.ACCESS_ADSERVICES_ATTRIBUTION",
    "android.permission.ACCESS_ADSERVICES_CUSTOM_AUDIENCE",
    "android.permission.ACCESS_ADSERVICES_TOPICS",
    "com.applovin.array.apphub.permission.BIND_APPHUB_SERVICE",
    "com.google.android.gms.permission.AD_ID",
)

private val AD_ACTIVITIES = setOf(
    "com.amazon.aps.ads.activity.ApsInterstitialActivity",
    "com.amazon.device.ads.DTBInterstitialActivity",
    "com.applovin.adview.AppLovinFullscreenActivity",
    "com.applovin.adview.AppLovinFullscreenImmersiveActivity",
    "com.applovin.creative.MaxCreativeDebuggerActivity",
    "com.applovin.creative.MaxCreativeDebuggerDisplayedAdActivity",
    "com.applovin.mediation.MaxDebuggerActivity",
    "com.applovin.mediation.MaxDebuggerAdUnitDetailActivity",
    "com.applovin.mediation.MaxDebuggerAdUnitWaterfallsListActivity",
    "com.applovin.mediation.MaxDebuggerAdUnitsListActivity",
    "com.applovin.mediation.MaxDebuggerCmpNetworksListActivity",
    "com.applovin.mediation.MaxDebuggerDetailActivity",
    "com.applovin.mediation.MaxDebuggerMultiAdActivity",
    "com.applovin.mediation.MaxDebuggerTcfConsentStatusesListActivity",
    "com.applovin.mediation.MaxDebuggerTcfInfoListActivity",
    "com.applovin.mediation.MaxDebuggerTcfStringActivity",
    "com.applovin.mediation.MaxDebuggerTestLiveNetworkActivity",
    "com.applovin.mediation.MaxDebuggerTestModeNetworkActivity",
    "com.applovin.mediation.MaxDebuggerUnifiedFlowActivity",
    "com.applovin.mediation.MaxDebuggerWaterfallSegmentsActivity",
    "com.applovin.sdk.AppLovinWebViewActivity",
    "com.bytedance.sdk.openadsdk.activity.TTAdActivity",
    "com.bytedance.sdk.openadsdk.activity.TTAppOpenAdActivity",
    "com.bytedance.sdk.openadsdk.activity.TTCeilingLandingPageActivity",
    "com.bytedance.sdk.openadsdk.activity.TTDelegateActivity",
    "com.bytedance.sdk.openadsdk.activity.TTFullScreenExpressVideoActivity",
    "com.bytedance.sdk.openadsdk.activity.TTFullScreenVideoActivity",
    "com.bytedance.sdk.openadsdk.activity.TTInterstitialActivity",
    "com.bytedance.sdk.openadsdk.activity.TTInterstitialExpressActivity",
    "com.bytedance.sdk.openadsdk.activity.TTLandingPageActivity",
    "com.bytedance.sdk.openadsdk.activity.TTPlayableLandingPageActivity",
    "com.bytedance.sdk.openadsdk.activity.TTRewardExpressVideoActivity",
    "com.bytedance.sdk.openadsdk.activity.TTRewardVideoActivity",
    "com.bytedance.sdk.openadsdk.activity.TTVideoLandingPageLink2Activity",
    "com.bytedance.sdk.openadsdk.activity.TTWebsiteActivity",
    "com.chartboost.sdk.internal.clickthrough.EmbeddedBrowserActivity",
    "com.chartboost.sdk.view.CBImpressionActivity",
    "com.chartboost.sdk.view.FullscreenAdActivity",
    "com.facebook.ads.AudienceNetworkActivity",
    "com.five_corp.ad.AdActivity",
    "com.five_corp.ad.AdReportDialogActivity",
    "com.fyber.inneractive.sdk.activities.FyberReportAdActivity",
    "com.fyber.inneractive.sdk.activities.InneractiveFullscreenAdActivity",
    "com.fyber.inneractive.sdk.activities.InneractiveInternalBrowserActivity",
    "com.fyber.inneractive.sdk.activities.InneractiveRichMediaVideoPlayerActivityCore",
    "com.fyber.inneractive.sdk.activities.InternalStoreWebpageActivity",
    "com.google.android.gms.ads.AdActivity",
    "com.google.android.gms.ads.NotificationHandlerActivity",
    "com.google.android.gms.ads.OutOfContextTestingActivity",
    "com.inmobi.ads.rendering.InMobiAdActivity",
    "com.ironsource.mediationsdk.testSuite.TestSuiteActivity",
    "com.ironsource.sdk.controller.ControllerActivity",
    "com.ironsource.sdk.controller.InterstitialActivity",
    "com.ironsource.sdk.controller.OpenUrlActivity",
    "com.mbridge.msdk.activity.MBCommonActivity",
    "com.mbridge.msdk.out.LoadingActivity",
    "com.mbridge.msdk.reward.player.MBRewardVideoActivity",
    "com.moloco.sdk.xenoss.sdkdevkit.android.adrenderer.internal.mraid.MraidActivity",
    "com.moloco.sdk.xenoss.sdkdevkit.android.adrenderer.internal.staticrenderer.StaticAdActivity",
    "com.moloco.sdk.xenoss.sdkdevkit.android.adrenderer.internal.templates.renderer.fullscreen.FullscreenWebviewActivity",
    "com.moloco.sdk.xenoss.sdkdevkit.android.adrenderer.internal.vast.VastActivity",
    "com.unity3d.ads.adplayer.FullScreenWebViewDisplay",
    "com.unity3d.services.ads.adunit.AdUnitActivity",
    "com.unity3d.services.ads.adunit.AdUnitSoftwareActivity",
    "com.unity3d.services.ads.adunit.AdUnitTransparentActivity",
    "com.unity3d.services.ads.adunit.AdUnitTransparentSoftwareActivity",
    "com.vungle.ads.internal.ui.VungleActivity",
)

private val AD_SERVICES = setOf(
    "com.applovin.impl.adview.activity.FullscreenAdService",
    "com.bytedance.sdk.openadsdk.multipro.aidl.BinderPoolService",
    "com.chartboost.sdk.internal.video.repository.exoplayer.VideoRepositoryDownloadService",
    "com.google.android.gms.ads.AdService",
)

private val AD_RECEIVERS = setOf(
    "com.mbridge.msdk.foundation.same.broadcast.NetWorkChangeReceiver",
)

private val AD_PROVIDERS = setOf(
    "com.applovin.sdk.AppLovinInitProvider",
    "com.facebook.ads.AudienceNetworkContentProvider",
    "com.google.android.gms.ads.MobileAdsInitProvider",
    "com.ironsource.lifecycle.IronsourceLifecycleProvider",
    "com.ironsource.lifecycle.LevelPlayActivityLifecycleProvider",
    "com.vungle.ads.VungleProvider",
    "droom.daro.a.DaroAInitializer",
)

/** Removes advertising identifiers and ad SDK components from Noji. */
@Suppress("unused")
val removeAdsPatch = patch(
    name = "Remove Ads",
    description = "Removes advertising identifiers and ad SDK components from Noji.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_NOJI)

    execute {
        editManifest {
            removePermissions(AD_PERMISSIONS)
            removeComponents("activity", AD_ACTIVITIES)
            removeComponents("service", AD_SERVICES)
            removeComponents("receiver", AD_RECEIVERS)
            removeComponents("provider", AD_PROVIDERS)
        }
    }
}
