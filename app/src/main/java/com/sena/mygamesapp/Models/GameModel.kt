package com.sena.mygamesapp.Models

import com.google.gson.annotations.SerializedName

//a class that holds the design of the gameList and the data of the elements that will populate that list
data class GameModel(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("background_image") val backgroundImage: String,
    @SerializedName("metacritic") val metacritic: Int,
)
