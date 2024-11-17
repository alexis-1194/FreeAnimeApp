package com.example.jikanapp.data.remote

import com.example.jikanapp.domain.model.full.Anime

data class AnimeResponse(
    val pagination: Any,
    val data: ArrayList<Anime>
)
