package com.example.jikanapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jikanapp.domain.usecase.GetPopularAnimesUseCase

//class AnimeViewModelFactory(private val getPopularAnimesUseCase: GetPopularAnimesUseCase) : ViewModelProvider.Factory {
//
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(AnimeViewModel::class.java)) {
//            return AnimeViewModel(getPopularAnimesUseCase) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}