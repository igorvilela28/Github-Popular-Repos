package com.igorvd.githubrepo.presentation.pull_requests

import com.igorvd.githubrepo.data.GitHubRepo
import com.igorvd.githubrepo.data.PullRequest
import com.igorvd.githubrepo.presentation.BasePresenter
import com.igorvd.githubrepo.presentation.BaseView

/**
 * Defines a contract to be respected by the presenter-view interaction of the MVP
 * @author Igor Vilela
 * @since 17/10/17
 */
interface PullRequestsContract {

    interface Presenter : BasePresenter {

        suspend fun loadOpenPullRequests(
                ownerLogin: String,
                repoName: String,
                currentItemsSize: Int = 0)

        suspend fun loadClosedPullRequests(
                ownerLogin: String,
                repoName: String,
                currentItemsSize: Int = 0)

    }

    interface View : BaseView {

        fun showOpenPullRequests(pullRequests : List<PullRequest>)

        fun onOpenEmpty()

        fun clearVisibleList()

    }
}