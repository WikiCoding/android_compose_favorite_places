package com.wikicoding.favoriteplaces.uiscreens

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.wikicoding.favoriteplaces.imageservice.ImgSharedPrefs
import com.wikicoding.favoriteplaces.R
import com.wikicoding.favoriteplaces.data.PlaceElement
import com.wikicoding.favoriteplaces.viewmodel.MainViewModel

@Composable
fun AddView(navHostController: NavHostController, mainViewModel: MainViewModel) {
    var placeNameState by remember { mutableStateOf("") }
    var placeDescriptionState by remember { mutableStateOf("") }
    var placeAddressState by remember { mutableStateOf("") }
    var placeImageUrlState by remember { mutableStateOf("") }
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

//            Button(onClick = {
//
//            }) {
//                Text(text = "Get my Current Location")
//            }
        }
    }
}
