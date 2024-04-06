package com.wikicoding.favoriteplaces.db

import android.content.Context
import androidx.room.Room
import com.wikicoding.favoriteplaces.data.PlacesDatabase
import com.wikicoding.favoriteplaces.data.PlacesRepository

object DbGraph {
    private lateinit var placesDatabase: PlacesDatabase

    val placesRepository by lazy {
        PlacesRepository(placesDatabase.placesDao())
    }

    fun provide(context: Context) {
        placesDatabase = Room.databaseBuilder(
            context,
            PlacesDatabase::class.java,
            "compose_places_database.db"
        ).build()
    }
}