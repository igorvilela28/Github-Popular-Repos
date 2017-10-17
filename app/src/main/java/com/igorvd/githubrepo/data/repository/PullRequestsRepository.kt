package com.igorvd.githubrepo.data.repository

import com.igorvd.githubrepo.domain.entities.PullRequest
import com.igorvd.githubrepo.domain.exceptions.RepositoryException

/**
 * @author Igor Vilela
 * @since 12/10/17
 */
interface PullRequestsRepository {

    /**
     * @throws RepositoryException
     */
    @Throws(RepositoryException::class)
    suspend fun loadPullRequests(
            ownerLogin: String,
            repoName: String,
            page: Int = 1) : List<PullRequest>
}