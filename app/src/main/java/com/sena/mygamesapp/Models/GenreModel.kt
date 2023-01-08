package com.sena.mygamesapp.Models

import com.google.gson.annotations.SerializedName

// Create GenreModel for getting Genre list item in game list
data class GenreModel(
    @SerializedName("id") var id: Int,
    @SerializedName("name") var name: String,
)

