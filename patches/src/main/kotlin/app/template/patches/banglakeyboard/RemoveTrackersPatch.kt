package app.template.patches.banglakeyboard

import app.morphe.patcher.patch.patch

private val TRACKER_SERVICES = setOf(
    "com.deshkeyboard.analytics.firebase.messaging.FireBaseNotificationHelper",
    "com.google.android.datatransport.runtime.backends.TransportBackendDiscovery",
    "com.google.android.datatransport.runtime.scheduling.jobscheduling.JobInfoSchedulerService",
    "com.google.android.gms.measurement.AppMeasurementJobService",
    "com.google.android.gms.measurement.AppMeasurementService",
    "com.google.firebase.components.ComponentDiscoveryService",
    "com.google.firebase.messaging.FirebaseMessagingService",
    "com.google.firebase.sessions.SessionLifecycleService",
)

private val TRACKER_RECEIVERS = setOf(
    "com.facebook.AuthenticationTokenManager\$CurrentAuthenticationTokenChangedBroadcastReceiver",
    "com.facebook.CurrentAccessTokenExpirationBroadcastReceiver",
    "com.google.android.datatransport.runtime.scheduling.jobscheduling.AlarmManagerSchedulerBroadcastReceiver",
    "com.google.android.gms.measurement.AppMeasurementReceiver",
    "com.google.firebase.iid.FirebaseInstanceIdReceiver",
)

private val TRACKER_PROVIDERS = setOf(
    "com.facebook.internal.FacebookInitProvider",
    "com.google.firebase.provider.FirebaseInitProvider",
)

private val TRACKER_PERMISSIONS = setOf(
    "com.google.android.c2dm.permission.RECEIVE",
    "com.google.android.finsky.permission.BIND_GET_INSTALL_REFERRER_SERVICE",
)

private val TRACKER_META_DATA = setOf(
    "backend:com.google.android.datatransport.cct.CctBackendFactory",
    "com.facebook.sdk.ApplicationId",
    "com.facebook.sdk.AutoInitEnabled",
    "com.facebook.sdk.AutoLogAppEventsEnabled",
    "com.facebook.sdk.ClientToken",
    "com.google.firebase.components:com.google.firebase.abt.component.AbtRegistrar",
    "com.google.firebase.components:com.google.firebase.analytics.connector.internal.AnalyticsConnectorRegistrar",
    "com.google.firebase.components:com.google.firebase.crashlytics.CrashlyticsRegistrar",
    "com.google.firebase.components:com.google.firebase.crashlytics.FirebaseCrashlyticsKtxRegistrar",
    "com.google.firebase.components:com.google.firebase.crashlytics.ndk.CrashlyticsNdkRegistrar",
    "com.google.firebase.components:com.google.firebase.datatransport.TransportRegistrar",
    "com.google.firebase.components:com.google.firebase.installations.FirebaseInstallationsKtxRegistrar",
    "com.google.firebase.components:com.google.firebase.installations.FirebaseInstallationsRegistrar",
    "com.google.firebase.components:com.google.firebase.messaging.FirebaseMessagingKtxRegistrar",
    "com.google.firebase.components:com.google.firebase.messaging.FirebaseMessagingRegistrar",
    "com.google.firebase.components:com.google.firebase.perf.FirebasePerfKtxRegistrar",
    "com.google.firebase.components:com.google.firebase.perf.FirebasePerfRegistrar",
    "com.google.firebase.components:com.google.firebase.remoteconfig.FirebaseRemoteConfigKtxRegistrar",
    "com.google.firebase.components:com.google.firebase.remoteconfig.RemoteConfigRegistrar",
)

/** Removes telemetry services, receivers, providers, permissions and metadata. */
@Suppress("unused")
val removeTrackersPatch = patch(
    name = "Remove Trackers",
    description = "Removes analytics, crash reporting, messaging and install-referrer components.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_BANGLA_KEYBOARD)

    execute {
        editManifest {
            removeComponents("service", TRACKER_SERVICES)
            removeComponents("receiver", TRACKER_RECEIVERS)
            removeComponents("provider", TRACKER_PROVIDERS)
            removePermissions(TRACKER_PERMISSIONS)
            removeMetaData(TRACKER_META_DATA)
        }
    }
}
