package com.igorvd.githubrepo.data

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