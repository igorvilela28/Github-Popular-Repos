package com.igorvd.githubrepo.dependency_injection.ui

import com.igorvd.githubrepo.MainActivity
import com.igorvd.githubrepo.presentation.popular_repos.PopularReposContract
import com.igorvd.githubrepo.presentation.popular_repos.PopularReposPresenter
import dagger.Binds
import dagger.Module

/**
 * @author Igor Vilela
 * @since 14/10/17
 */
@Module
abstract class MainModule {

    @Binds
    abstract fun providesView(mainActivity: MainActivity) : PopularReposContract.View

    @Binds
    abstract fun providesPresenter(presenter: PopularReposPresenter) : PopularReposContract.Presenter

}