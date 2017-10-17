package com.igorvd.githubrepo.domain

import com.igorvd.githubrepo.domain.entities.PullRequest
import com.igorvd.githubrepo.data.repository.PullRequestsRepository
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async

/**
 * Interactor that load a list of [PullRequest] objects.
 * It's the interactor responsibility to decides what page should be retrieved.
 * It makes sure that the access to the
 * repository is made by a background thread, using the coroutine dispatcher [CommonPool]
 * @author Igor Vilela
 * @since 17/10/17
 */
class LoadPullRequestsInteractor(private val pullRequestsRepository: PullRequestsRepository) :
        Interactor<List<PullRequest>, LoadPullRequestsInteractor.Params> {

    suspend override fun execute(params: Params): Deferred<List<PullRequest>> = async(CommonPool) {

        if(isLastPage(params)) {
            ArrayList<PullRequest>()
        } else {

            val page = getPageToSearch(params)
            pullRequestsRepository.loadPullRequests(
                    ownerLogin = params.ownerLogin,
                    repoName = params.repoName,
                    page = page)
        }
    }

    private fun getPageToSearch(params: Params): Int =
            (params.currentItemsSize / params.pageSize) + 1

    private fun isLastPage(params: Params) : Boolean =
            params.currentItemsSize % params.pageSize > 0

    class Params (
            val ownerLogin: String,
            val repoName: String,
            val currentItemsSize: Int,
            val pageSize: Int = DEFAULT_ITEMS_PER_PAGE
    )
}