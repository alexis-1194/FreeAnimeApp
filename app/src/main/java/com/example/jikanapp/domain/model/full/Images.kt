package com.example.jikanapp.domain.model.full


import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Images(
    val jpg: Jpg,
    val webp: Webp
) : Parcelable