package com.sena.mygamesapp.Models

import com.google.gson.annotations.SerializedName


data class GameModel(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("background_image") val backgroundImage: String,
    @SerializedName("metacritic") val metacritic: Int,
)
