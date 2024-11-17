package com.example.jikanapp.domain.usecase

import com.example.jikanapp.data.repository.AnimeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetPopularAnimesUseCase(animeRepository: AnimeRepository): GetPopularAnimesUseCase {
        return GetPopularAnimesUseCase(animeRepository)
    }

    @Provides
    fun provideGetAnimeByNameUseCase(animeRepository: AnimeRepository): GetAnimeByNameUseCase {
        return GetAnimeByNameUseCase(animeRepository)
    }

    @Provides
    fun provideAnimeRepository(): AnimeRepository {
        return AnimeRepository()
    }
}