package com.igorvd.githubrepo.dependency_injection.core

import com.igorvd.githubrepo.MainActivity
import com.igorvd.githubrepo.dependency_injection.domain.GitHubRepoModule
import com.igorvd.githubrepo.dependency_injection.ui.MainModule
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
            MainModule::class))
    abstract fun contributeMainActivity() : MainActivity;

}