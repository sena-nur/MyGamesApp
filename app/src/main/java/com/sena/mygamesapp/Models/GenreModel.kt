package com.sena.mygamesapp.Models

import com.google.gson.annotations.SerializedName

// Create GenreModel for getting Genre list item in game list
data class GenreModel(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
)

