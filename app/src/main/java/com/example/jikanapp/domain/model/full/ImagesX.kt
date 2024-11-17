package com.example.jikanapp.domain.model.full


import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class ImagesX(
    val image_url: String?,
    val large_image_url: String?,
    val maximum_image_url: String?,
    val medium_image_url: String?,
    val small_image_url: String?
) : Parcelable