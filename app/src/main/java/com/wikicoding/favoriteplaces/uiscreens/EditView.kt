package com.wikicoding.favoriteplaces.uiscreens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.wikicoding.favoriteplaces.R
import com.wikicoding.favoriteplaces.imageservice.ImgSharedPrefs
import com.wikicoding.favoriteplaces.data.PlaceElement
import com.wikicoding.favoriteplaces.viewmodel.MainViewModel
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun EditView(navHostController: NavHostController, id: Int, mainViewModel: MainViewModel) {
    val currPlace = mainViewModel.findById(id).collectAsState(
        initial = PlaceElement(0, "", "", "", 0.0, 0.0, ""))

    mainViewModel.elementStateName = currPlace.value.name
    mainViewModel.elementStateDescription = currPlace.value.description
    mainViewModel.elementStateAddress = currPlace.value.address
    mainViewModel.elementStateImage = currPlace.value.photoUri
    mainViewModel.elementStateLatitude = currPlace.value.lat
    mainViewModel.elementStateLongitude = currPlace.value.lng
    val context = LocalContext.current.applicationContext

    val scaffoldState = rememberScaffoldState()

    val photoFromGalleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            val imgBitmap = ImgSharedPrefs.convertUriToBitmap(uri!!, context)
            val finalUri = ImgSharedPrefs.saveImageToInternalStorage(context, imgBitmap!!)
            mainViewModel.elementStateImage = finalUri.toString()
        })

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                mainViewModel.updateElement(
                    PlaceElement(
                        currPlace.value.id,
                        mainViewModel.elementStateName,
                        mainViewModel.elementStateDescription,
                        mainViewModel.elementStateAddress,
                        mainViewModel.elementStateLatitude,
                        mainViewModel.elementStateLongitude,
                        mainViewModel.elementStateImage
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
                value = mainViewModel.elementStateName,
                onValueChange = { mainViewModel.onNameChange(it) },
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
                value = mainViewModel.elementStateAddress,
                onValueChange = { mainViewModel.onAddressChange(it) },
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
                value = mainViewModel.elementStateDescription,
                onValueChange = { mainViewModel.onDescriptionChange(it) },
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
                            mainViewModel.elementStateLatitude = loc.latitude
                            mainViewModel.elementStateLongitude = loc.longitude
                            Geocoder(context, Locale.getDefault())
                                .getFromLocation(
                                    mainViewModel.elementStateLatitude,
                                    mainViewModel.elementStateLongitude,
                                    1,
                                    Geocoder.GeocodeListener { addr ->
                                        mainViewModel.elementStateAddress = addr[0].getAddressLine(0)
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
                Text(text = "Edit photo")
            }

            AsyncImage(
                model = mainViewModel.elementStateImage,
                contentDescription = "imageFromWeb",
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp)),
                placeholder = painterResource(id = R.drawable.baseline_photo_camera_24),
                error = painterResource(id = R.drawable.baseline_hide_image_24),
            )
        }
    }
}