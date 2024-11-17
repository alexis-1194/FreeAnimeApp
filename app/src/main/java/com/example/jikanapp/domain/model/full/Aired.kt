package com.example.jikanapp.domain.model.full


import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Aired(
    val from: String?,
    val prop: Prop,
    val string: String?,
    val to: String?
) : Parcelable