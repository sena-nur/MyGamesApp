package com.sena.mygamesapp.Models

import com.google.gson.annotations.SerializedName

data class GameDetailModel(@SerializedName("id") val id: Int,
                           @SerializedName("name") val name: String,
                           @SerializedName("description") val description: String,
                           @SerializedName("reddit_url") val reddit_url: String,
                           @SerializedName("website") val website: String,
                           @SerializedName("background_image") val background_image: String)
