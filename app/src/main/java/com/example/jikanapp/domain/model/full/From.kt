package com.example.jikanapp.domain.model.full


import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class From(
    val day: Int,
    val month: Int,
    val year: Int
) : Parcelable