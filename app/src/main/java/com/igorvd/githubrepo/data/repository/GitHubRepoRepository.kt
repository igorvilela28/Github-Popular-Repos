package com.igorvd.githubrepo.data.repository

import com.igorvd.githubrepo.domain.entities.GitHubRepo
import com.igorvd.githubrepo.domain.exceptions.RepositoryException

/**
 * @author Igor Vilela
 * @since 12/10/17
 */
interface GitHubRepoRepository {

    /**
     * @throws RepositoryException
     */
    @Throws(RepositoryException::class)
    suspend fun loadRepositories(
            language : String? = "java",
            sort: String="stars",
            order: String="desc",
            page: Int = 1) : List<GitHubRepo>
}