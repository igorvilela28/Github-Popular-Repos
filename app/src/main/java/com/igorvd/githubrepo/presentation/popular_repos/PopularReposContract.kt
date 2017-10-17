package com.igorvd.githubrepo.presentation.popular_repos

import com.igorvd.githubrepo.domain.entities.GitHubRepo
import com.igorvd.githubrepo.presentation.BasePresenter
import com.igorvd.githubrepo.presentation.BaseView

/**
 * Defines a contract to be respected by the presenter-view interaction of the MVP
 * @author Igor Vilela
 * @since 12/10/17
 */
interface PopularReposContract {

    interface Presenter : BasePresenter {

        /**
         * Load a list of repositories
         * @param[currentItemsSize] the current visible list size on screen size
         */
        suspend fun loadRepositories(currentItemsSize: Int = 0)

    }

    interface View : BaseView {

        /**
         * Show the loaded repositories list
         */
        fun showRepositories(repositories : List<GitHubRepo>)

    }

}