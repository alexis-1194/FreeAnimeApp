package com.example.jikanapp.domain.model.full


import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Trailer(
    val embed_url: String?,
    val images: ImagesX,
    val url: String?,
    val youtube_id: String?
) : Parcelable