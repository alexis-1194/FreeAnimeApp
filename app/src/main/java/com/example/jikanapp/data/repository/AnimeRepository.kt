package com.example.jikanapp.data.repository

import com.example.jikanapp.data.remote.RetrofitInstance
import com.example.jikanapp.domain.model.full.Anime
import com.example.jikanapp.util.Utils
import javax.inject.Inject

class AnimeRepository @Inject constructor() {
    suspend fun getPopularAnimes(): List<Anime> {
        var dayOfWeek = Utils.getCurrentDayOfWeek()
        return RetrofitInstance.api_popular.getPopularAnimes(dayOfWeek).data
    }
    suspend fun getListAnime(name : String): List<Anime> {
        return RetrofitInstance.api_search.getListAnime(name).data
    }
}
