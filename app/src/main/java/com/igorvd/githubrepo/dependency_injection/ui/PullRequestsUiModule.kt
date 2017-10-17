package com.igorvd.githubrepo.dependency_injection.ui

import com.igorvd.githubrepo.presentation.popular_repos.PopularReposActivity
import com.igorvd.githubrepo.presentation.popular_repos.PopularReposContract
import com.igorvd.githubrepo.presentation.popular_repos.PopularReposPresenter
import com.igorvd.githubrepo.presentation.pull_requests.PullRequestsActivity
import com.igorvd.githubrepo.presentation.pull_requests.PullRequestsContract
import com.igorvd.githubrepo.presentation.pull_requests.PullRequestsPresenter
import dagger.Binds
import dagger.Module

/**
 * @author Igor Vilela
 * @since 14/10/17
 */
@Module
abstract class PullRequestsUiModule {

    @Binds
    abstract fun providesView(pullRequestsActivity: PullRequestsActivity) : PullRequestsContract.View

    @Binds
    abstract fun providesPresenter(presenter: PullRequestsPresenter) : PullRequestsContract.Presenter

}