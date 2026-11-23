package app.template.patches.camscanner

import app.morphe.patcher.patch.patch

private val NETWORK_PERMISSIONS = setOf(
    "android.permission.ACCESS_ADSERVICES_AD_ID",
    "android.permission.ACCESS_ADSERVICES_ATTRIBUTION",
    "android.permission.ACCESS_ADSERVICES_CUSTOM_AUDIENCE",
    "android.permission.ACCESS_ADSERVICES_TOPICS",
    "android.permission.ACCESS_NETWORK_STATE",
    "android.permission.ACCESS_WIFI_STATE",
    "android.permission.CHANGE_WIFI_MULTICAST_STATE",
    "android.permission.FOREGROUND_SERVICE_DATA_SYNC",
    "android.permission.READ_BASIC_PHONE_STATE",
    "com.google.android.c2dm.permission.RECEIVE",
    "com.google.android.finsky.permission.BIND_GET_INSTALL_REFERRER_SERVICE",
    "com.google.android.gms.permission.AD_ID",
)

private val NETWORK_SERVICES = setOf(
    "com.bytedance.apm6.traffic.TrafficTransportService",
    "com.google.android.datatransport.runtime.backends.TransportBackendDiscovery",
    "com.google.android.datatransport.runtime.scheduling.jobscheduling.JobInfoSchedulerService",
    "com.google.android.gms.measurement.AppMeasurementJobService",
    "com.google.android.gms.measurement.AppMeasurementService",
    "com.google.firebase.messaging.FirebaseMessagingService",
    "com.google.firebase.sessions.SessionLifecycleService",
    "com.intsig.camscanner.onelinkresource.OnelinkResourceForegroundService",
    "com.intsig.camscanner.service.FaxQueryService",
    "com.intsig.camscanner.service.FaxService",
    "com.intsig.camscanner.service.UploadService",
    "com.intsig.push.fcm.MyFirebaseMessagingService",
    "com.mbridge.msdk.config.activity.MBAdTaskWatchService",
)

private val NETWORK_RECEIVERS = setOf(
    "com.appsflyer.SingleInstallBroadcastReceiver",
    "com.facebook.AuthenticationTokenManager\$CurrentAuthenticationTokenChangedBroadcastReceiver",
    "com.facebook.CurrentAccessTokenExpirationBroadcastReceiver",
    "com.google.android.datatransport.runtime.scheduling.jobscheduling.AlarmManagerSchedulerBroadcastReceiver",
    "com.google.android.gms.measurement.AppMeasurementReceiver",
    "com.google.firebase.iid.FirebaseInstanceIdReceiver",
    "com.intsig.camscanner.business.push.PushMsgReceiver",
    "com.intsig.camscanner.onecloud.BoxActionReceiver",
    "com.mbridge.msdk.foundation.same.broadcast.NetWorkChangeReceiver",
)

private val NETWORK_PROVIDERS = setOf(
    "com.bytedance.frameworks.core.apm.contentprovider.MonitorContentProvider",
    "com.facebook.ads.AudienceNetworkContentProvider",
    "com.facebook.internal.FacebookInitProvider",
    "com.intsig.camscanner.util.environment.RumProvider",
    "com.mbridge.msdk.config.component.status.MBComponentLifecycleProvider",
    "com.vk.api.sdk.internal.VKInitProvider",
    "com.vungle.ads.VungleProvider",
    "io.sentry.android.core.SentryPerformanceProvider",
)

private val NETWORK_FLAGS = mapOf(
    "firebase_analytics_collection_enabled" to "false",
    "firebase_performance_logcat_enabled" to "false",
    "google_analytics_deferred_deep_link_enabled" to "false",
)

/** Removes background upload, push, ad and telemetry entry points that expect network access. */
@Suppress("unused")
val removeNetworkComponentsPatch = patch(
    name = "Remove Network Components",
    description = "Removes upload, push, ad and telemetry components that expect network access, and disables remaining analytics flags.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_CAMSCANNER)

    execute {
        editManifest {
            removePermissions(NETWORK_PERMISSIONS)
            removeComponents("service", NETWORK_SERVICES)
            removeComponents("receiver", NETWORK_RECEIVERS)
            removeComponents("provider", NETWORK_PROVIDERS)
            NETWORK_FLAGS.forEach { (name, value) -> setMetaData(setOf(name), value) }
        }
    }
}
