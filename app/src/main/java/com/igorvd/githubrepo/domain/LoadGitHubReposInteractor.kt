package com.igorvd.githubrepo.domain

import com.igorvd.githubrepo.data.GitHubRepo
import com.igorvd.githubrepo.data.GitHubRepoRepository
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async

/**
 * Interactor that load a list of [GitHubRepo] objects. It makes sure that the access to the
 * repository is made by a background thread, using the coroutine dispatcher [CommonPool]
 * @author Igor Vilela
 * @since 13/10/17
 */
class LoadGitHubReposInteractor(private val gitHubRepository : GitHubRepoRepository) :
        Interactor<List<GitHubRepo>> {

    suspend override fun execute(): Deferred<List<GitHubRepo>> = async(CommonPool) {

        gitHubRepository.loadRepositories()

    }

}