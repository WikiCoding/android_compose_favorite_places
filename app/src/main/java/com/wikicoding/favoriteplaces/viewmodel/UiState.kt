package com.wikicoding.favoriteplaces.viewmodel

import android.os.Parcelable
import com.wikicoding.favoriteplaces.data.PlaceElement

data class UiState(
    var element: List<PlaceElement>? = null,
)
