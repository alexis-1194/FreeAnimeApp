package com.example.jikanapp.domain.model.original

import android.os.Parcel
import android.os.Parcelable

data class Image(
    val jpg: Jpg?,
    val webp: Webp?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Jpg::class.java.classLoader), // Deserializar Jpg
        parcel.readParcelable(Webp::class.java.classLoader) // Deserializar Webp
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(jpg, flags) // Serializar Jpg
        parcel.writeParcelable(webp, flags) // Serializar Webp
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Image> {
        override fun createFromParcel(parcel: Parcel): Image {
            return Image(parcel)
        }

        override fun newArray(size: Int): Array<Image?> {
            return arrayOfNulls(size)
        }
    }
}
