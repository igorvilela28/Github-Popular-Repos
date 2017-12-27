package com.igorvd.githubrepo.domain.entities

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


/**
 * @author Igor Vilela
 * @since 17/10/17
 */
data class PullRequest(

        @SerializedName("id")
        val id: Int,
        @SerializedName("user")
        val owner: Owner,
        @SerializedName("html_url")
        val htmlUrl: String,
        @SerializedName("number")
        val number: Int,
        @SerializedName("state")
        val state: String,
        @SerializedName("locked")
        val locked: Boolean,
        @SerializedName("title")
        val title: String,
        @SerializedName("body")
        val body: String,
        @SerializedName("created_at")
        val createdAt: String

) : Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readParcelable<Owner>(Owner::class.java.classLoader),
            source.readString(),
            source.readInt(),
            source.readString(),
            1 == source.readInt(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeParcelable(owner, 0)
        writeString(htmlUrl)
        writeInt(number)
        writeString(state)
        writeInt((if (locked) 1 else 0))
        writeString(title)
        writeString(body)
        writeString(createdAt)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<PullRequest> = object : Parcelable.Creator<PullRequest> {
            override fun createFromParcel(source: Parcel): PullRequest = PullRequest(source)
            override fun newArray(size: Int): Array<PullRequest?> = arrayOfNulls(size)
        }
    }
}