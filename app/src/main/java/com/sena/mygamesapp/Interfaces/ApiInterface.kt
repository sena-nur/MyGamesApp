package com.sena.mygamesapp.Interfaces

import com.sena.mygamesapp.Models.GameDetailModel
import com.sena.mygamesapp.Models.ResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface  {
    //Create ApiInterface which contain the api methods. Firstly, enter the method types with method names.
    //After, set query and path parameters for methods.
    //Call methods with our created Response models
    @GET("games")
    fun getGamesList(@Query("page_size") page_size: Int,
                     @Query("page") page: Int,
                     @Query("search") search: String,
                     @Query("key") key: String): Call<ResponseModel>

    @GET("games/{id}")
    fun getGameDetail(@Path("id") id: Int,
                      @Query("key") key: String): Call<GameDetailModel>
}



