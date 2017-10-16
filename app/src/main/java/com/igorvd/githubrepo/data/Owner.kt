package com.igorvd.githubrepo.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * @author Igor Vilela
 * @since 12/10/17
 */
data class Owner(
        @SerializedName("login") private val login: String,
        @SerializedName("id") private val id: Int,
        @SerializedName("avatar_url") private val avatarUrl: String,
        @SerializedName("gravatar_id") private val gravatarId: String,
        @SerializedName("url") private val url: String,
        @SerializedName("html_url") private val htmlUrl: String,
        @SerializedName("type") private val type: String,
        @SerializedName("site_admin") private val siteAdmin: Boolean
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(login)
        writeInt(id)
        writeString(avatarUrl)
        writeString(gravatarId)
        writeString(url)
        writeString(htmlUrl)
        writeString(type)
        writeInt((if (siteAdmin) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Owner> = object : Parcelable.Creator<Owner> {
            override fun createFromParcel(source: Parcel): Owner = Owner(source)
            override fun newArray(size: Int): Array<Owner?> = arrayOfNulls(size)
        }
    }
}