package com.example.jikanapp.domain.model.original

import android.os.Parcel
import android.os.Parcelable

data class Anime(
    val mal_id: Int,
    val title: String,
    val url: String,
    val images: Image?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readParcelable(Image::class.java.classLoader) // Deserializar el objeto Image
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(mal_id)
        parcel.writeString(title)
        parcel.writeString(url)
        parcel.writeParcelable(images, flags) // Serializar el objeto Image
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Anime> {
        override fun createFromParcel(parcel: Parcel): Anime {
            return Anime(parcel)
        }

        override fun newArray(size: Int): Array<Anime?> {
            return arrayOfNulls(size)
        }
    }
}
