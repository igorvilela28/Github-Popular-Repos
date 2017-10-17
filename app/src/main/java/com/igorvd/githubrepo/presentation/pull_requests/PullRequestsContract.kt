package com.igorvd.githubrepo.presentation.pull_requests

import com.igorvd.githubrepo.domain.entities.GitHubRepo
import com.igorvd.githubrepo.domain.entities.PullRequest
import com.igorvd.githubrepo.domain.entities.Owner
import com.igorvd.githubrepo.presentation.BasePresenter
import com.igorvd.githubrepo.presentation.BaseView

/**
 * Defines a contract to be respected by the presenter-view interaction of the MVP
 * @author Igor Vilela
 * @since 17/10/17
 */
interface PullRequestsContract {

    interface Presenter : BasePresenter {

        /**
         * Load the pull requests with the status "open" for the [GitHubRepo]
         * @param[ownerLogin] The [Owner.login]
         * @param[repoName] the [GitHubRepo.name]
         * @param[currentItemsSize] the current visible list size on screen size
         */
        suspend fun loadOpenPullRequests(
                ownerLogin: String,
                repoName: String,
                currentItemsSize: Int = 0)
    }

    interface View : BaseView {

        /**
         * Show the retrieve [PullRequest] list on screen
         */
        fun showOpenPullRequests(pullRequests : List<PullRequest>)

        /**
         * Indicates to the view that the asked list is empty
         */
        fun onOpenEmpty()

        /**
         * Clear the list visible to the user
         */
        fun clearVisibleList()

    }
}