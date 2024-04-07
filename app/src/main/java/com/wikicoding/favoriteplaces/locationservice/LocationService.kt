package com.wikicoding.favoriteplaces.locationservice

import android.content.Context.LOCATION_SERVICE
import android.location.LocationManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
fun IsLocationEnabled(): Boolean {
    val context = LocalContext.current
    val locationManager = remember(context) {
        context.getSystemService(LOCATION_SERVICE) as LocationManager
    }
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}