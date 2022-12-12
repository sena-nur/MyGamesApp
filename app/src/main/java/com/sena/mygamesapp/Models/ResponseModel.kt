package com.sena.mygamesapp.Models

import com.google.gson.annotations.SerializedName

//Create ResponseModel for getting game list in api
//The all games in "results" as GameModel list are populated in RecyclerView
class ResponseModel (@SerializedName("count") val count: Int,
                     @SerializedName("next")val next:String,
                     @SerializedName("results") val results:MutableList<GameModel>):java.io.Serializable

