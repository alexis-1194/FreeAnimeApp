package com.example.jikanapp.domain.model.full


import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Broadcast(
    val day: String?,
    val string: String?,
    val time: String?,
    val timezone: String?
) : Parcelable