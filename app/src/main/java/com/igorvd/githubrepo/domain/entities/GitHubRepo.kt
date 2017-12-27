package com.igorvd.githubrepo.domain.entities

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


/**
 * @author Igor Vilela
 * @since 12/10/17
 */
data class GitHubRepo(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("full_name") val fullName: String,
        @SerializedName("owner") val owner: Owner,
        @SerializedName("") val isPrivate: Boolean,
        @SerializedName("html_url") val htmlUrl: String,
        @SerializedName("description") val description: String,
        @SerializedName("size") val size: Int,
        @SerializedName("stargazers_count") val stargazersCount: Int,
        @SerializedName("watchers_count") val watchersCount: Int,
        @SerializedName("language") val language: String,
        @SerializedName("forks_count") val forksCount: Int,
        @SerializedName("forks") val forks: Int,
        @SerializedName("score") val score: Int
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readParcelable(Owner::class.java.classLoader),
            1 == source.readInt(),
            source.readString(),
            source.readString(),
            source.readInt(),
            source.readInt(),
            source.readInt(),
            source.readString(),
            source.readInt(),
            source.readInt(),
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeString(name)
        writeString(fullName)
        writeParcelable(owner, flags)
        writeInt((if (isPrivate) 1 else 0))
        writeString(htmlUrl)
        writeString(description)
        writeInt(size)
        writeInt(stargazersCount)
        writeInt(watchersCount)
        writeString(language)
        writeInt(forksCount)
        writeInt(forks)
        writeInt(score)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<GitHubRepo> = object : Parcelable.Creator<GitHubRepo> {
            override fun createFromParcel(source: Parcel): GitHubRepo = GitHubRepo(source)
            override fun newArray(size: Int): Array<GitHubRepo?> = arrayOfNulls(size)
        }
    }
}