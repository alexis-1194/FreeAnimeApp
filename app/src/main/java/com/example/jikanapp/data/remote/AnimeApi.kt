package com.example.jikanapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface AnimeApi {
    @GET
    suspend fun getPopularAnimes(@Url url: String): AnimeResponse

    @GET("anime")
    suspend fun getListAnime(@Query("q") name: String): AnimeResponse
}

