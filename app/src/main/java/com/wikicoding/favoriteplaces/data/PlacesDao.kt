package com.wikicoding.favoriteplaces.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PlacesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun add(placeElement: PlaceElement)

    @Query("SELECT * FROM places")
    abstract fun getAll(): Flow<List<PlaceElement>>

    @Query("SELECT * FROM places WHERE id = :id")
    abstract fun findById(id: Int): Flow<PlaceElement>

    @Update
    abstract suspend fun update(placeElement: PlaceElement)

    @Delete
    abstract fun delete(placeElement: PlaceElement)
}