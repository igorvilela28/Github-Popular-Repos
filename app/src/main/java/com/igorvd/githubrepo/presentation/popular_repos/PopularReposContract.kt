package com.igorvd.githubrepo.presentation.popular_repos

import com.igorvd.githubrepo.data.GitHubRepo

/**
 * Defines a contract to be respected by the presenter-view interaction of the MVP
 * @author Igor Vilela
 * @since 12/10/17
 */
interface PopularReposContract {

    interface Presenter {

        suspend fun loadRepositories(
                language : String?="java",
                sort: String="stars",
                order: String="desc",
                page: Int = 1)

    }

    interface View {

        fun showRepositories(repositories : List<GitHubRepo>)
        fun showProgress();
        fun hideProgress();
        fun showError();

    }

}