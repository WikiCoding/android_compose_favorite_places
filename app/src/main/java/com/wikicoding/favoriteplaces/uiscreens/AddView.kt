package com.wikicoding.favoriteplaces.uiscreens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Geocoder.GeocodeListener
import android.location.LocationManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.wikicoding.favoriteplaces.R
import com.wikicoding.favoriteplaces.data.PlaceElement
import com.wikicoding.favoriteplaces.imageservice.ImgSharedPrefs
import com.wikicoding.favoriteplaces.viewmodel.MainViewModel
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AddView(navHostController: NavHostController, mainViewModel: MainViewModel) {
    var placeNameState by remember { mutableStateOf("") }
    var placeDescriptionState by remember { mutableStateOf("") }
    var placeAddressState by remember { mutableStateOf("") }
    var placeImageUrlState by remember { mutableStateOf("") }
    var latitudeState by remember { mutableStateOf(0.0) }
    var longitudeState by remember { mutableStateOf(0.0) }
    val context = LocalContext.current.applicationContext

    val scaffoldState = rememberScaffoldState()

    val photoFromGalleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            val imgBitmap = ImgSharedPrefs.convertUriToBitmap(uri!!, context)
            val finalUri = ImgSharedPrefs.saveImageToInternalStorage(context, imgBitmap!!)
            placeImageUrlState = finalUri.toString()
        })

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                mainViewModel.addElement(
                    PlaceElement(
                        0,
                        placeNameState,
                        placeAddressState,
                        placeDescriptionState,
                        0.0,
                        0.0,
                        placeImageUrlState
                    )
                )

                navHostController.navigateUp()
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = placeNameState,
                onValueChange = { placeNameState = it },
                label = { Text(text = "Place Name") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = MaterialTheme.colorScheme.primary,
                    backgroundColor = MaterialTheme.colorScheme.background,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.primary
                )
            )

            OutlinedTextField(
                value = placeAddressState,
                onValueChange = { placeAddressState = it },
                label = { Text(text = "Place Address") },
                singleLine = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = MaterialTheme.colorScheme.primary,
                    backgroundColor = MaterialTheme.colorScheme.background,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.primary
                )
            )

            OutlinedTextField(
                value = placeDescriptionState,
                onValueChange = { placeDescriptionState = it },
                label = { Text(text = "Place Description") },
                singleLine = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = MaterialTheme.colorScheme.primary,
                    backgroundColor = MaterialTheme.colorScheme.background,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.primary
                )
            )

            Button(
                onClick = {
                    val fusedLocationClient: FusedLocationProviderClient =
                        LocationServices.getFusedLocationProviderClient(context)

                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        Toast.makeText(
                            context,
                            "Permissions for Location not granted!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        location?.let { loc ->
                            latitudeState = loc.latitude
                            longitudeState = loc.longitude
                            Geocoder(context, Locale.getDefault())
                                .getFromLocation(
                                    latitudeState,
                                    longitudeState,
                                    1,
                                    GeocodeListener { addr ->
                                        placeAddressState = addr[0].getAddressLine(0)
                                    })
                        }
                    }

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(text = "Get my Current Location")
            }

            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), onClick = {
                photoFromGalleryLauncher.launch(
                    PickVisualMediaRequest(
                        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }) {
                Text(text = "Add photo from Gallery")
            }

            AsyncImage(
                model = placeImageUrlState, contentDescription = null, modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp)),
                placeholder = painterResource(id = R.drawable.baseline_photo_camera_24),
                error = painterResource(id = R.drawable.baseline_hide_image_24)
            )
        }
    }
}
