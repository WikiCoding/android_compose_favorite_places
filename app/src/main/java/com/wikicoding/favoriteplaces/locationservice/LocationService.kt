package com.wikicoding.favoriteplaces.locationservice

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult

class LocationService(private var context: Context) {
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    fun getLocation(fusedLocationClient: FusedLocationProviderClient) {
        val priority = 100
        val interval: Long = 1000
        val maxUpdates = 1
        requestLocationData(fusedLocationClient, priority, interval, maxUpdates)
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationData(fusedLocationClient: FusedLocationProviderClient, priority: Int,
                                    intervalMillis: Long, maxUpdates: Int) {
        val locationRequest = LocationRequest.Builder(priority, intervalMillis).setMaxUpdates(maxUpdates).build()

        fusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper())
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location? = locationResult.lastLocation
            latitude = mLastLocation?.latitude!!
            longitude = mLastLocation.longitude

            requestLocation(latitude, longitude)
        }
    }

    private fun requestLocation(latitude: Double, longitude: Double) {
        val address = GetAddressFromLatLng(context, latitude, longitude)

        address.setAddressListener(object : GetAddressFromLatLng.AddressListener {
            override fun onAddressFound(address: String?) {
                address.toString()
                latitude.toString()
                longitude.toString()
                Log.e("address", address.toString())
            }

            override fun onError() {

            }
        })

        address.getAddress()
    }

    @Composable
    fun isLocationEnabled(): Boolean {
        val context = LocalContext.current
        val locationManager = remember(context) {
            context.getSystemService(LOCATION_SERVICE) as LocationManager
        }
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}