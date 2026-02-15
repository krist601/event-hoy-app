package org.jkc.event.tracker.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun BindLocationPermission(
    onGranted: () -> Unit,
    onDenied: () -> Unit
): PermissionControl {
    return remember {
        object : PermissionControl {
            override fun launchPermissionRequest() {
                // For iOS, the LocationService will typically trigger the OS prompt
                // when location is accessed. Or we could implement CLLocationManager logic here.
                // For now, we proceed to onGranted to let the service try.
                onGranted()
            }
        }
    }
}
