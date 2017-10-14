package com.igorvd.githubrepo.data

import com.google.gson.annotations.SerializedName


/**
 * @author Igor Vilela
 * @since 12/10/17
 */
data class GitHubRepo (
        @SerializedName("id") val id: Int,
        @SerializedName("name")  val name: String,
        @SerializedName("full_name")  val fullName: String,
        @SerializedName("owner")  val owner: Owner,
        @SerializedName("")  val isPrivate: Boolean,
        @SerializedName("html_url")  val htmlUrl: String,
        @SerializedName("description")  val description: String,
        @SerializedName("size")  val size: Int,
        @SerializedName("stargazers_count")  val stargazersCount: Int,
        @SerializedName("watchers_count")  val watchersCount: Int,
        @SerializedName("language")  val language: String,
        @SerializedName("forks_count")  val forksCount: Int,
        @SerializedName("forks")  val forks: Int,
        @SerializedName("score")  val score: Int
)