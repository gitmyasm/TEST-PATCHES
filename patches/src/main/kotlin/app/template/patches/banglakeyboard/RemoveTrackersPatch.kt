package app.template.patches.banglakeyboard

import app.morphe.patcher.patch.patch

private val TRACKER_PERMISSIONS = setOf(
    "com.google.android.finsky.permission.BIND_GET_INSTALL_REFERRER_SERVICE",
)

private val TRACKER_SERVICES = setOf(
    "com.deshkeyboard.analytics.firebase.messaging.FireBaseNotificationHelper",
    "com.google.android.datatransport.runtime.backends.TransportBackendDiscovery",
    "com.google.android.datatransport.runtime.scheduling.jobscheduling.JobInfoSchedulerService",
)

private val TRACKER_RECEIVERS = setOf(
    "com.facebook.AuthenticationTokenManager\$CurrentAuthenticationTokenChangedBroadcastReceiver",
    "com.facebook.CurrentAccessTokenExpirationBroadcastReceiver",
    "com.google.android.datatransport.runtime.scheduling.jobscheduling.AlarmManagerSchedulerBroadcastReceiver",
)

/**
 * The app initializes Firebase Analytics and Crashlytics in its Application
 * class, so the init providers and component registrars must stay in place.
 * Disabling collection through metadata stops the tracking without breaking
 * startup.
 */
private val TRACKER_FLAGS = mapOf(
    "com.facebook.sdk.AdvertiserIDCollectionEnabled" to "false",
    "com.facebook.sdk.AutoInitEnabled" to "false",
    "com.facebook.sdk.AutoLogAppEventsEnabled" to "false",
    "firebase_analytics_collection_deactivated" to "true",
    "firebase_crashlytics_collection_enabled" to "false",
    "google_analytics_adid_collection_enabled" to "false",
    "google_analytics_ssaid_collection_enabled" to "false",
)

/** Disables telemetry while keeping the SDK initialization the app depends on. */
@Suppress("unused")
val removeTrackersPatch = patch(
    name = "Remove Trackers",
    description = "Disables analytics, crash reporting and ad-identifier collection while keeping app startup intact.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_BANGLA_KEYBOARD)

    execute {
        editManifest {
            removePermissions(TRACKER_PERMISSIONS)
            removeComponents("service", TRACKER_SERVICES)
            removeComponents("receiver", TRACKER_RECEIVERS)
            TRACKER_FLAGS.forEach { (name, value) -> setMetaData(setOf(name), value) }
        }
    }
}
