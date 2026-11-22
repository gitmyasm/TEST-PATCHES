package app.template.patches.noji

import app.morphe.patcher.patch.patch

private val TRACKER_PERMISSIONS = setOf(
    "com.google.android.finsky.permission.BIND_GET_INSTALL_REFERRER_SERVICE",
)

private val TRACKER_SERVICES = setOf(
    "com.google.android.datatransport.runtime.backends.TransportBackendDiscovery",
    "com.google.android.datatransport.runtime.scheduling.jobscheduling.JobInfoSchedulerService",
    "com.google.android.gms.measurement.AppMeasurementJobService",
    "com.google.android.gms.measurement.AppMeasurementService",
    "com.google.firebase.sessions.SessionLifecycleService",
)

private val TRACKER_RECEIVERS = setOf(
    "com.google.android.datatransport.runtime.scheduling.jobscheduling.AlarmManagerSchedulerBroadcastReceiver",
    "com.google.android.gms.measurement.AppMeasurementReceiver",
)

private val TRACKER_PROVIDERS = setOf(
    "co.ab180.airbridge.internal.lifecycle.LifecycleProvider",
    "com.datadog.android.rum.DdRumContentProvider",
    "io.invertase.firebase.crashlytics.ReactNativeFirebaseCrashlyticsInitProvider",
    "io.sentry.android.core.SentryInitProvider",
    "io.sentry.android.core.SentryPerformanceProvider",
)

private val TRACKER_META_DATA = setOf(
    "app_data_collection_default_enabled",
    "firebase_analytics_collection_enabled",
    "google_analytics_adid_collection_enabled",
    "google_analytics_automatic_screen_reporting_enabled",
    "google_analytics_default_allow_ad_personalization_signals",
    "google_analytics_default_allow_ad_storage",
    "google_analytics_default_allow_ad_user_data",
    "google_analytics_default_allow_analytics_storage",
    "google_analytics_ssaid_collection_enabled",
)

/** Metadata that is already false in this version; force it so future inputs cannot re-enable it. */
private val TRACKER_META_DATA_ALREADY_FALSE = setOf(
    "com.facebook.sdk.AdvertiserIDCollectionEnabled",
    "com.facebook.sdk.AutoInitEnabled",
    "com.facebook.sdk.AutoLogAppEventsEnabled",
    "delivery_metrics_exported_to_big_query_enabled",
    "firebase_analytics_collection_deactivated",
    "firebase_crashlytics_collection_enabled",
    "io.sentry.auto-init",
)

/** Removes analytics, install-referrer, crash, and diagnostics components from Noji. */
@Suppress("unused")
val removeTrackersPatch = patch(
    name = "Remove Trackers",
    description = "Removes analytics, install-referrer, crash, and diagnostics components from Noji.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_NOJI)

    execute {
        editManifest {
            removePermissions(TRACKER_PERMISSIONS)
            removeComponents("service", TRACKER_SERVICES)
            removeComponents("receiver", TRACKER_RECEIVERS)
            removeComponents("provider", TRACKER_PROVIDERS)
            setMetaData(TRACKER_META_DATA, "false")
            setMetaData(TRACKER_META_DATA_ALREADY_FALSE, "false")
        }
    }
}
