package com.wikicoding.favoriteplaces.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wikicoding.favoriteplaces.data.InMemoryDb
import com.wikicoding.favoriteplaces.data.PlaceElement
import com.wikicoding.favoriteplaces.data.PlacesRepository
import com.wikicoding.favoriteplaces.db.DbGraph
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val placesRepository: PlacesRepository = DbGraph.placesRepository
) : ViewModel() {
    private val _elementsState = MutableStateFlow(SingleElementState())
    val elementsState = _elementsState.asStateFlow()
    var elementStateName by mutableStateOf("")
    var elementStateDescription by mutableStateOf("")
    var elementStateAddress by mutableStateOf("")
    var elementStateImage by mutableStateOf("")

    lateinit var getAll: Flow<List<PlaceElement>>

    init {
        viewModelScope.launch {
            getAll = placesRepository.getAll()
        }
    }

    fun addElement(placeElement: PlaceElement) {
        viewModelScope.launch(Dispatchers.IO) {
            placesRepository.add(placeElement)
        }
    }

    fun deleteElement(placeElement: PlaceElement) {
        viewModelScope.launch(Dispatchers.IO) {
            placesRepository.delete(placeElement)
        }
    }

    fun findById(id: Int): Flow<PlaceElement> {
        return placesRepository.findById(id)
    }

    fun updateElement(placeElement: PlaceElement) {
        viewModelScope.launch(Dispatchers.IO) {
            placesRepository.update(placeElement)
        }
    }

    fun onNameChange(it: String) {
        elementStateName = it
    }

    fun onAddressChange(it: String) {
        elementStateAddress = it
    }

    fun onDescriptionChange(it: String) {
        elementStateDescription = it
    }

    fun onImageChange(it: String) {
        elementStateImage = it
    }

//    val visiblePermissionDialogQueue = mutableStateListOf<String>()
//
//    fun dismissDialog() {
//        visiblePermissionDialogQueue.removeFirst()
//    }
//
//    fun onPermissionResult(
//        permission: String,
//        isGranted: Boolean
//    ) {
//        if (!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
//            visiblePermissionDialogQueue.add(permission)
//        }
//    }
}