package com.igorvd.githubrepo.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



/**
 * @author Igor Vilela
 * @since 12/10/17
 */
data class Owner (
        @SerializedName("login") private val login: String,
        @SerializedName("id") private val id: Int,
        @SerializedName("avatar_url") private val avatarUrl: String,
        @SerializedName("gravatar_id") private val gravatarId: String,
        @SerializedName("url") private val url: String,
        @SerializedName("html_url") private val htmlUrl: String,
        @SerializedName("type") private val type: String,
        @SerializedName("site_admin") private val siteAdmin: Boolean
)