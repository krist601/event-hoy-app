package org.jkc.event.tracker.presentation.util

import androidx.compose.runtime.Composable

@Composable
expect fun BindLocationPermission(
    onGranted: () -> Unit,
    onDenied: () -> Unit
): PermissionControl

interface PermissionControl {
    fun launchPermissionRequest()
}
