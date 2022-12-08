package com.sena.mygamesapp.Interfaces

import com.sena.mygamesapp.Models.GameModel
import com.sena.mygamesapp.Models.ResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface  {
    @GET("games")
    fun getGamesList(@Query("page_size") page_size: Int,
                     @Query("page") page: Int,
                     @Query("key") key: String): Call<ResponseModel>
}