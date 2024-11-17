package com.example.jikanapp.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val URL_POPULAR = "https://api.jikan.moe/v4/schedules/"

    val api_popular: AnimeApi by lazy {
        Retrofit.Builder()
            .baseUrl(URL_POPULAR)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AnimeApi::class.java)
    }

//    val api_popular: AnimeApi = Retrofit.Builder()
//        .baseUrl(URL_POPULAR)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//        .create(AnimeApi::class.java)


    private const val URL_SEARCH = "https://api.jikan.moe/v4/"

    val api_search: AnimeApi by lazy {
        Retrofit.Builder()
            .baseUrl(URL_SEARCH)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AnimeApi::class.java)
    }


}