package com.igorvd.githubrepo.domain

import com.igorvd.githubrepo.domain.entities.GitHubRepo
import com.igorvd.githubrepo.data.repository.GitHubRepoRepository
import com.igorvd.githubrepo.domain.LoadGitHubReposInteractor.Params
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async

/**
 * Interactor that load a list of [GitHubRepo] objects.
 * It's the interactor responsibility to decides what page should be retrieved.
 * It makes sure that the access to the
 * repository is made by a background thread, using the coroutine dispatcher [CommonPool]
 * @author Igor Vilela
 * @since 13/10/17
 */
class LoadGitHubReposInteractor(private val gitHubRepository : GitHubRepoRepository) :
        Interactor<List<GitHubRepo>, Params?> {

    suspend override fun execute(params: Params?): Deferred<List<GitHubRepo>> = async(CommonPool) {

        if(params != null) {

            val page = getPageToSearch(params)
            gitHubRepository.loadRepositories(page = page)

        } else {

            gitHubRepository.loadRepositories()
        }

    }

    private fun getPageToSearch(params: Params): Int=(params.currentItemsSize / params.pageSize) + 1


    class Params (
            val currentItemsSize: Int,
            val pageSize: Int = DEFAULT_ITEMS_PER_PAGE
    )
}