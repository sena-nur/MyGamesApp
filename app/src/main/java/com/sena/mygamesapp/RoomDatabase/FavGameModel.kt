package com.sena.mygamesapp.RoomDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.sena.mygamesapp.Models.GenreModel

@Entity(tableName = "games_table")
data class FavGameModel(
    @PrimaryKey(autoGenerate = true)
    var uid: Int,

    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "background_image")
    var background_image: String,

    @ColumnInfo(name = "metacritic")
    var metacritic: Int,

    @ColumnInfo(name = "genres")
    val genres: String
)
