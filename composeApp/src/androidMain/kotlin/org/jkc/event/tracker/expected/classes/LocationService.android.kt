package org.jkc.event.tracker.expected.classes

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.location.Location
import org.jkc.event.tracker.domain.entity.LocationEntity
import org.jkc.event.tracker.expected.interfaces.ILocationService
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

import org.jkc.event.tracker.presentation.util.ContextProvider

@SuppressLint("MissingPermission")
actual class LocationService : ILocationService {

    private val appContext: Context
        get() = ContextProvider.context

    fun initLocationService(context: Context) {
        // No longer needed due to ContextProvider, kept for compatibility
    }

    actual override suspend fun getCurrentLocation(): LocationEntity? {
        // ContextProvider guarantees context availability

        android.util.Log.d("LocationDebug", "LocationService: getCurrentLocation called")
        
        // Basic permission check
        val hasFine = androidx.core.content.ContextCompat.checkSelfPermission(
            appContext, 
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        
        val hasCoarse = androidx.core.content.ContextCompat.checkSelfPermission(
            appContext, 
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED

        if (!hasFine && !hasCoarse) return null

        return suspendCancellableCoroutine { cont ->
            try {
                val locationManager = appContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

                if (!isGpsEnabled && !isNetworkEnabled) {
                    cont.resume(null)
                    return@suspendCancellableCoroutine
                }

                // Try to get last known location first for speed
                val location = if (isGpsEnabled) {
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                } else {
                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                }

                if (location != null) {
                    cont.resume(location.toEntity())
                } else {
                    val locationListener = object : android.location.LocationListener {
                        override fun onLocationChanged(location: Location) {
                            if (cont.isActive) {
                                cont.resume(location.toEntity())
                            }
                            locationManager.removeUpdates(this)
                        }
                        override fun onProviderDisabled(provider: String) {}
                        override fun onProviderEnabled(provider: String) {}
                        override fun onStatusChanged(provider: String?, status: Int, extras: android.os.Bundle?) {}
                    }
                    
                    val provider = if (isGpsEnabled) LocationManager.GPS_PROVIDER else LocationManager.NETWORK_PROVIDER
                    
                    // Request single update
                    locationManager.requestLocationUpdates(
                        provider,
                        0L,
                        0f,
                        locationListener,
                        android.os.Looper.getMainLooper()
                    )
                    
                    cont.invokeOnCancellation {
                        locationManager.removeUpdates(locationListener)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                if (cont.isActive) cont.resume(null)
            }
        }
    }

    private fun Location.toEntity(): LocationEntity {
        return LocationEntity(
            id = -1, // Dummy ID for current location
            name = "Mi Ubicación",
            address = null,
            latitude = this.latitude,
            longitude = this.longitude,
            url = null
        )
    }
}
