package com.igorvd.githubrepo.data

import com.igorvd.githubrepo.domain.exceptions.RepositoryException
import com.igorvd.githubrepo.network.GitHubApi
import com.igorvd.githubrepo.network.ApiClientBuilder
import com.igorvd.githubrepo.network.GitHubRepoResponse
import com.igorvd.githubrepo.network.requests.SynchronousRequestManagerImpl
import retrofit2.Call
import javax.inject.Inject

/**
 * @author Igor Vilela
 * @since 12/10/17
 */
class GitHubRepoNetworkRepository
@Inject
constructor(val gitHubApi: GitHubApi,
            val syncRequestManager: SynchronousRequestManagerImpl<GitHubRepoResponse>)

    : GitHubRepoRepository {

    val LANGUAGE_PREFIX = "language:";

    @Throws(RepositoryException::class)
    override suspend fun loadRepositories(language: String?, sort: String, order: String, page: Int):
            List<GitHubRepo>  {

            val newLanguage = LANGUAGE_PREFIX + language

            val getRepos: Call<GitHubRepoResponse> = gitHubApi.getPopularRepos(newLanguage)

           return syncRequestManager.getResult(getRepos).items
    }

}