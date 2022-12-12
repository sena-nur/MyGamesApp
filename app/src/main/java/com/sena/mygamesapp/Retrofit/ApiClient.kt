package com.sena.mygamesapp.Retrofit

import com.sena.mygamesapp.AppConstants.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    //Create client instance from OKHTTPClient
    private val client = OkHttpClient.Builder().build()
    //Create Retrofit builder with Base Url in AppConstants and created client instance
    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
    //To create service call, we  called this function from other classes
    fun <T> buildService(service:Class<T>): T {
        return retrofit.create(service)
    }
}