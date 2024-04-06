package com.wikicoding.favoriteplaces.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class PlaceElement(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String,
    var description: String = "",
    var address: String,
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var photoUri: String = ""
    )