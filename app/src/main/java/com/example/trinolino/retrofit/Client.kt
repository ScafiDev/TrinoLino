package com.example.trinolino.retrofit

import com.example.trinolino.retrofit.RailManagerApi.Companion.BASE_URL
import com.example.trinolino.retrofit.RailManagerApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Client {
    val retrofit : RailManagerApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RailManagerApi::class.java)
    }
}