package com.example.jikanapp.domain.usecase

import com.example.jikanapp.data.repository.AnimeRepository
import com.example.jikanapp.domain.model.full.Anime
import javax.inject.Inject

class GetAnimeByNameUseCase @Inject constructor(
    private val repository: AnimeRepository
) {
    suspend fun execute(name: String): List<Anime> {
        return repository.getListAnime(name)
    }
}