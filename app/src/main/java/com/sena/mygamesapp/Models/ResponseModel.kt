package com.sena.mygamesapp.Models

import com.google.gson.annotations.SerializedName

class ResponseModel (@SerializedName("count") val count: Int,
                     @SerializedName("next")val next:String,
                     @SerializedName("results") val results:MutableList<GameModel>):java.io.Serializable