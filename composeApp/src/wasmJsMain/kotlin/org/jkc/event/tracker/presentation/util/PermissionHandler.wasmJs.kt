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
                onGranted()
            }
        }
    }
}
