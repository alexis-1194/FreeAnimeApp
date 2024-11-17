package com.example.jikanapp.domain.model.full


import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Webp(
    val image_url: String?,
    val large_image_url: String?,
    val small_image_url: String?
) : Parcelable