package com.wikicoding.favoriteplaces.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PlaceElement::class], version = 1, exportSchema = false)
abstract class PlacesDatabase: RoomDatabase() {
    abstract fun placesDao(): PlacesDao
}