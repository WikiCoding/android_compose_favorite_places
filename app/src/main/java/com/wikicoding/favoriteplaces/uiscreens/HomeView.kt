package com.wikicoding.favoriteplaces.uiscreens

import android.Manifest
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.rememberDismissState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.wikicoding.favoriteplaces.data.PlaceElement
import com.wikicoding.favoriteplaces.navigation.Screen
import com.wikicoding.favoriteplaces.permissions.RequestPermissions
import com.wikicoding.favoriteplaces.viewmodel.MainViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeView(navController: NavHostController, mainViewModel: MainViewModel) {
    val elements = mainViewModel.getAll.collectAsState(initial = listOf())

    val permissionsToRequest = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    val multiplePermissionResultLauncher = RequestPermissions(mainViewModel = mainViewModel)

    val scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Screen.AddScreen.route) }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }) {
        var swipedElement by remember { mutableStateOf<PlaceElement?>(null) }
        var showDialog by remember { mutableStateOf(false) }
        multiplePermissionResultLauncher.launch(permissionsToRequest)

        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(0.8f)) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    items(items = elements.value, key = { item -> item.id }) { place ->
                        val dismissState = rememberDismissState(
                            confirmStateChange = {
                                if (it == DismissValue.DismissedToEnd) {
                                    showDialog = true
                                    swipedElement = place
                                }
                                if (it == DismissValue.DismissedToStart) {
                                    val id = place.id
                                    navController.navigate(Screen.EditScreen.route + "/$id")
                                }
                                true
                            }
                        )

                        SwipeToDismiss(state = dismissState, background = {
                            val color by animateColorAsState(
                                if (dismissState.dismissDirection == DismissDirection.StartToEnd) Color.Red
                                else if (dismissState.dismissDirection == DismissDirection.EndToStart) Color.Green
                                else Color.Transparent,
                                label = ""
                            )

                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .background(color)
                                    .padding(horizontal = 20.dp),
                            ) {
                                if (dismissState.dismissDirection == DismissDirection.StartToEnd) {
                                    Icon(
                                        Icons.Default.Delete, contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.align(Alignment.CenterStart)
                                    )
                                } else {
                                    Icon(
                                        Icons.Default.Edit, contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.align(Alignment.CenterEnd)
                                    )
                                }
                            }
                        },
                            directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
                            dismissThresholds = { FractionalThreshold(0.25f) },
                            dismissContent = {
                                LazyColumnElement(element = place)
                            }
                        )

                        /** reset the dismissState after consuming the updateStateChange and
                         * reset the dismissState of the Delete swipe after cancel **/
                        if (dismissState.isDismissed(DismissDirection.EndToStart) || !showDialog) {
                            LaunchedEffect(key1 = place) {
                                dismissState.reset()
                                dismissState.reset()
                                /** double reset solves the background color for some reason **/
                            }
                        }
                    }
                }
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showDialog = false
                        swipedElement = null
                    },
                    confirmButton = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(onClick = {
                                swipedElement?.let {
                                    mainViewModel.deleteElement(it)
                                }
                                showDialog = false
                                swipedElement = null
                            }) {
                                Text(text = "DELETE")
                            }

                            Button(onClick = {
                                showDialog = false
                                swipedElement = null
                                navController.navigateUp()
                            }) {
                                Text(text = "CANCEL")
                            }
                        }
                    },
                    title = { Text(text = "Are you sure you want to delete?") }
                )
            }
        }
    }
}