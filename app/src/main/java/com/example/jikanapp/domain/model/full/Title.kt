package com.example.jikanapp.domain.model.full


import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Title(
    val title: String?,
    val type: String?
) : Parcelable