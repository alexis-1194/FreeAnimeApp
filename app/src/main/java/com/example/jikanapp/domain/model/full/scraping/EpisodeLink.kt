package com.example.jikanapp.domain.model.full.scraping

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EpisodeLink(
    val title : String,
    var link : String,
    val releaseDate : String
) : Parcelable {
}

