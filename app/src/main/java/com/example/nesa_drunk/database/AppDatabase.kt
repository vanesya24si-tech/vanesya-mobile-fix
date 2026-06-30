package com.example.nesa_drunk.database

import android.content.Context

// Stub: AppDatabase digantikan oleh VillageDatabase
// Dipertahankan agar tidak ada import error
object AppDatabase {
    fun getDatabase(context: Context): VillageDatabase = VillageDatabase.getInstance(context)
}
