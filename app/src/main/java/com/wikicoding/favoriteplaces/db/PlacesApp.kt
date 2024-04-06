package com.wikicoding.favoriteplaces.db

import android.app.Application

class PlacesApp: Application() {
    override fun onCreate() {
        super.onCreate()
        DbGraph.provide(this)
    }
}