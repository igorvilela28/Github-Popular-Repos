package com.igorvd.githubrepo.data.repository.impl

import com.igorvd.githubrepo.domain.entities.PullRequest
import com.igorvd.githubrepo.data.repository.PullRequestsRepository
import com.igorvd.githubrepo.domain.exceptions.RepositoryException
import com.igorvd.githubrepo.network.GitHubApi
import com.igorvd.githubrepo.network.requests.SynchronousRequestManager
import retrofit2.Call
import javax.inject.Inject

/**
 * @author Igor Vilela
 * @since 12/10/17
 */
class PullRequestsNetworkRepository
@Inject
constructor(val gitHubApi: GitHubApi,
            val syncRequestManager: SynchronousRequestManager<List<PullRequest>>)

    : PullRequestsRepository {

    @Throws(RepositoryException::class)
    suspend override fun loadPullRequests(ownerLogin: String, repoName: String, page: Int): List<PullRequest> {

        val getPullRequests: Call<List<PullRequest>> = gitHubApi
                .getPullRequests(ownerLogin, repoName, page=page)

        return syncRequestManager.getResult(getPullRequests)

    }

}