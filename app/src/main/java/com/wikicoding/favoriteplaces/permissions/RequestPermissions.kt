package com.wikicoding.favoriteplaces.permissions

import android.Manifest
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.wikicoding.favoriteplaces.viewmodel.MainViewModel

@Composable
fun RequestPermissions(mainViewModel: MainViewModel): ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>> {
    val permissionsToRequest = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            permissionsToRequest.forEach { permission ->
                mainViewModel.onPermissionResult(
                    permission = permission,
                    isGranted = permissions[permission] == true
                )
            }
        }
    )

    return multiplePermissionResultLauncher
}