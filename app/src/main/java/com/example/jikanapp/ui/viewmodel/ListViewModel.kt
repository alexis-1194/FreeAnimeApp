package com.example.jikanapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jikanapp.domain.model.full.Anime
import com.example.jikanapp.domain.usecase.GetAnimeByNameUseCase
import com.example.jikanapp.ui.state.UiState
import com.example.jikanapp.ui.view.MainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val ANIME_VIEW_MODEL = "ListViewModel"

@HiltViewModel
class ListViewModel @Inject constructor(
    private val getAnimeByNameUseCase: GetAnimeByNameUseCase,
    private val savedStateHandle: SavedStateHandle,
) :
    ViewModel() {

    private val _animeStateFlowSearchByName =
        MutableStateFlow<UiState<List<Anime>>>(UiState.Loading)
    val animeStateFlowSearchByName: StateFlow<UiState<List<Anime>>> = _animeStateFlowSearchByName

    init {
        fetchByNameAnime()
    }


    fun fetchByNameAnime() {
        val name = savedStateHandle.get<String>(MainActivity.NOMBRE_ANIME) ?: return
        viewModelScope.launch {
            _animeStateFlowSearchByName.value = UiState.Loading
            try {
                val animes = getAnimeByNameUseCase.execute(name)
                _animeStateFlowSearchByName.value = UiState.Success(animes)
                Log.i(
                    this@ListViewModel.javaClass.name,
                    Thread.currentThread().stackTrace[1].methodName + "Obtuvo: " + _animeStateFlowSearchByName.value.toString()
                )
            } catch (e: Exception) {
                Log.e(
                    Thread.currentThread().stackTrace[1].className,
                    Thread.currentThread().stackTrace[1].methodName + " Error: $e"
                )
                _animeStateFlowSearchByName.value = UiState.Error("Error al cargar los datos")
            }
        }
    }
}