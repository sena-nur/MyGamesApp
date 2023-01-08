package com.sena.mygamesapp.RoomDatabase

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ViewedGameModel::class], version = 2)
abstract class ViewedGameDatabase : RoomDatabase() {
    abstract fun viewedGameDao(): ViewedGameDao
}