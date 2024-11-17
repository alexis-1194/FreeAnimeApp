package com.example.jikanapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jikanapp.domain.model.full.Anime
import com.example.jikanapp.domain.usecase.GetAnimeByNameUseCase
import com.example.jikanapp.domain.usecase.GetPopularAnimesUseCase
import com.example.jikanapp.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val ANIME_VIEW_MODEL = "AnimeViewModel"
private const val ANIME_VIEW_MODEL_fetchPopularAnimes = "fetchPopularAnimes"

@HiltViewModel
class AnimeViewModel @Inject constructor(
    private val getPopularAnimesUseCase: GetPopularAnimesUseCase,
    private val getAnimeByNameUseCase: GetAnimeByNameUseCase,
) :
    ViewModel() {

    private val _animeStateFlowPopular = MutableStateFlow<UiState<List<Anime>>>(UiState.Loading)
    val animeStateFlowPopular: StateFlow<UiState<List<Anime>>> = _animeStateFlowPopular

    private val _animeStateFlowSearchByName =
        MutableStateFlow<UiState<List<Anime>>>(UiState.Loading)
    val animeStateFlowSearchByName: StateFlow<UiState<List<Anime>>> = _animeStateFlowSearchByName

    init {
        fetchPopularAnimes()
    }

    private fun fetchPopularAnimes() {
        viewModelScope.launch {
            _animeStateFlowPopular.value = UiState.Loading
            try {
                val animes = getPopularAnimesUseCase.execute()
                _animeStateFlowPopular.value = UiState.Success(animes)

                withContext(Dispatchers.Main) {
                    Log.i(this@AnimeViewModel.javaClass.name,
                        "fetchPopularAnimes " + " Obtuvo: " + _animeStateFlowPopular.value.toString())
                }
            } catch (e: Exception) {
                Log.e(
                    this@AnimeViewModel.javaClass.name,
                    ANIME_VIEW_MODEL_fetchPopularAnimes + " Error: $e"
                )
                _animeStateFlowPopular.value = UiState.Error("Error al cargar los datos")
            }
        }
    }

    fun fetchByNameAnime(name: String) {
        viewModelScope.launch {
            _animeStateFlowSearchByName.value = UiState.Loading
            try {
                val animes = getAnimeByNameUseCase.execute(name)
                _animeStateFlowSearchByName.value = UiState.Success(animes)
                Log.i(
                    ANIME_VIEW_MODEL,
                    Thread.currentThread().stackTrace[1].methodName + "Obtuvo: " + _animeStateFlowSearchByName.value.toString()
                )
            } catch (e: Exception) {
                Log.e(
                    ANIME_VIEW_MODEL,
                    Thread.currentThread().stackTrace[1].methodName + " Error: $e"
                )
                _animeStateFlowSearchByName.value = UiState.Error("Error al cargar los datos")
            }
        }
    }
}