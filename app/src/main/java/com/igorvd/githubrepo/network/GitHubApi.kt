package com.igorvd.githubrepo.network

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author Igor Vilela
 * @since 12/10/17
 */
interface GitHubApi {

    /**
     * @param [query] The search keywords, as well as any qualifiers. Default: "language:Java"
     * @param [sort] The sort field. One of stars, forks, or updated. Default: results are sorted
     * by stars.
     * @param [order] The sort order if sort parameter is provided. One of asc or desc. Default: desc
     * @param [page] the page offset for the query results
     */
    @GET("search/repositories")
    fun getPopularRepos(
            @Query("q") query: String="language:Java",
            @Query("sort") sort: String="stars",
            @Query("order") order: String="desc",
            @Query("page") page: Int = 1) : Call<GitHubRepoResponse>

}