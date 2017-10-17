package com.igorvd.githubrepo.network

import com.google.gson.annotations.SerializedName
import com.igorvd.githubrepo.domain.entities.GitHubRepo


/**
 * @author Igor Vilela
 * @since 12/10/17
 */
data class GitHubRepoResponse (
        @SerializedName("total_count") var totalCount: Int,
        @SerializedName("incomplete_results") var isIncompleteResults: Boolean,
        @SerializedName("items") var items: List<GitHubRepo>
)