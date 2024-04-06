package com.wikicoding.favoriteplaces.data

import kotlinx.coroutines.flow.Flow

class PlacesRepository(private val placesDao: PlacesDao): PlacesDao() {
    override suspend fun add(placeElement: PlaceElement) {
        placesDao.add(placeElement)
    }

    override fun getAll(): Flow<List<PlaceElement>> {
        return placesDao.getAll()
    }

    override fun findById(id: Int): Flow<PlaceElement> {
        return placesDao.findById(id)
    }

    override suspend fun update(placeElement: PlaceElement) {
        placesDao.update(placeElement)
    }

    override fun delete(placeElement: PlaceElement) {
        placesDao.delete(placeElement)
    }

}