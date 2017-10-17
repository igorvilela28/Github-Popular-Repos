package com.igorvd.githubrepo.data.repository.impl

import com.igorvd.githubrepo.domain.entities.GitHubRepo
import com.igorvd.githubrepo.data.repository.GitHubRepoRepository
import com.igorvd.githubrepo.domain.exceptions.RepositoryException
import com.igorvd.githubrepo.network.GitHubApi
import com.igorvd.githubrepo.network.GitHubRepoResponse
import com.igorvd.githubrepo.network.requests.SynchronousRequestManager
import retrofit2.Call
import javax.inject.Inject

/**
 * @author Igor Vilela
 * @since 12/10/17
 */
class GitHubRepoNetworkRepository
@Inject
constructor(val gitHubApi: GitHubApi,
            val syncRequestManager: SynchronousRequestManager<GitHubRepoResponse>)

    : GitHubRepoRepository {

    val LANGUAGE_PREFIX = "language:";

    @Throws(RepositoryException::class)
    override suspend fun loadRepositories(language: String?, sort: String, order: String, page: Int):
            List<GitHubRepo>  {

            val languageQuery = LANGUAGE_PREFIX + language

            val getRepos: Call<GitHubRepoResponse> = gitHubApi.getPopularRepos(query=languageQuery,
                    page=page)

           return syncRequestManager.getResult(getRepos).items
    }

}