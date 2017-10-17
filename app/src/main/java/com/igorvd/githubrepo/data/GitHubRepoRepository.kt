package com.igorvd.githubrepo.data

import com.igorvd.githubrepo.domain.exceptions.RepositoryException
import java.io.IOException

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