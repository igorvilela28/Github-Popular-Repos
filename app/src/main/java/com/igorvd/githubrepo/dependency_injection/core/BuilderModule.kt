package com.igorvd.githubrepo.dependency_injection.core

import com.igorvd.githubrepo.presentation.popular_repos.PopularReposActivity
import com.igorvd.githubrepo.dependency_injection.domain.GitHubRepoModule
import com.igorvd.githubrepo.dependency_injection.domain.PullRequestModule
import com.igorvd.githubrepo.dependency_injection.ui.PopularReposUiModule
import com.igorvd.githubrepo.dependency_injection.ui.PullRequestsUiModule
import com.igorvd.githubrepo.presentation.pull_requests.PullRequestsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector;

/**
 * Module that contains the [ContributesAndroidInjector] implementations for injecting the concrete
 * Android framework classes
 * @author Igor Vilela
 * @since 14/10/17
 */

@Module
abstract class BuilderModule {

    @ContributesAndroidInjector(modules = arrayOf(
            GitHubRepoModule::class,
            PopularReposUiModule::class))
    abstract fun contributePopularReposActivity() : PopularReposActivity

    @ContributesAndroidInjector(modules = arrayOf(
            PullRequestModule::class,
            PullRequestsUiModule::class))
    abstract fun contributePullRequestActivity() : PullRequestsActivity

}