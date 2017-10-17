package com.igorvd.githubrepo.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

import com.igorvd.githubrepo.domain.entities.GitHubRepo
import com.igorvd.githubrepo.domain.entities.Owner
import com.igorvd.githubrepo.domain.entities.PullRequest
import retrofit2.http.Path

/**
 * Interface for communication with the Github API.
 *
 * @see <a href="https://developer.github.com/v3/">https://developer.github.com/v3/</a>
 * @author Igor Vilela
 * @since 12/10/17
 */
interface GitHubApi {

    /**
     * Retrieves a [GitHubRepoResponse] object that contains a list of [GitHubRepo] objects
     * @param [query] The search keywords, as well as any qualifiers. Default: "language:Java"
     * @param [sort] The sort field. One of stars, forks, or updated. Default: results are sorted
     * by stars.
     * @param [order] The sort order if sort parameter is provided. One of asc or desc. Default: desc
     * @param [page] the page offset for the get results
     * @param [perPage] the number of results per page
     */
    @GET("search/repositories")
    fun getPopularRepos(
            @Query("q") query: String="language:Java",
            @Query("sort") sort: String="stars",
            @Query("order") order: String="desc",
            @Query("page") page: Int = 1,
            @Query("per_page") perPage: Int = 20) : Call<GitHubRepoResponse>


    /**
     *
     * @param[ownerLogin] the [Owner.login] value
     * @param[repoName] the [GitHubRepo.name] value
     * @param[state] Either open, closed, or all to filter by state. Default: open
     * @param[sort] What to sort results by. Can be either created, updated, popularity
     *              (comment count) or long-running (age, filtering by pulls updated in the last month).
     *              Default: created
     * @param[page] the page offset for the get results
     * @param[perPage] the number of results per page
     */
    @GET("repos/{owner}/{repo}/pulls")
    fun getPullRequests(
            @Path("owner") ownerLogin: String,
            @Path("repo") repoName: String,
            @Query("state") state: String="open",
            @Query("sort") sort: String="created",
            @Query("direction") direction: String= "desc",
            @Query("page") page: Int = 1,
            @Query("per_page") perPage: Int = 20) : Call<List<PullRequest>>


}