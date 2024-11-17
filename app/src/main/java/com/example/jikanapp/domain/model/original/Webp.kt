package com.example.jikanapp.domain.model.original

import android.os.Parcel
import android.os.Parcelable

data class Webp(
    val image_url: String,
    val small_image_url: String,
    val large_image_url: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(image_url)
        parcel.writeString(small_image_url)
        parcel.writeString(large_image_url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Webp> {
        override fun createFromParcel(parcel: Parcel): Webp {
            return Webp(parcel)
        }

        override fun newArray(size: Int): Array<Webp?> {
            return arrayOfNulls(size)
        }
    }
}
