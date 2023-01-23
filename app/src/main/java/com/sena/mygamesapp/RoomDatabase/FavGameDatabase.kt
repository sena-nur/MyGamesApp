package com.sena.mygamesapp.RoomDatabase

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavGameModel::class], version = 2)
//abstract class to keep favorite games in database
abstract class FavGameDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
}