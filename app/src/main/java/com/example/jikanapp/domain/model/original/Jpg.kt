package com.example.jikanapp.domain.model.original

import android.os.Parcel
import android.os.Parcelable

data class Jpg(
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

    companion object CREATOR : Parcelable.Creator<Jpg> {
        override fun createFromParcel(parcel: Parcel): Jpg {
            return Jpg(parcel)
        }

        override fun newArray(size: Int): Array<Jpg?> {
            return arrayOfNulls(size)
        }
    }
}
