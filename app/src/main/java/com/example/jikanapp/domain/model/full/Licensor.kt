package com.example.jikanapp.domain.model.full


import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Licensor(
    val mal_id: Int,
    val name: String?,
    val type: String?,
    val url: String?
) : Parcelable