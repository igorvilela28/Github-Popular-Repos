package com.igorvd.githubrepo.dependency_injection.core

import com.igorvd.githubrepo.domain.entities.PullRequest
import com.igorvd.githubrepo.network.ApiClientBuilder
import com.igorvd.githubrepo.network.GitHubApi
import com.igorvd.githubrepo.network.GitHubRepoResponse
import com.igorvd.githubrepo.network.requests.SynchronousRequestManager
import com.igorvd.githubrepo.network.requests.SynchronousRequestManagerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Igor Vilela
 * @since 14/10/17
 */

@Singleton
@Module
class NetworkModule {

    private val GITHUB_BASE_URL = "https://api.github.com/"

    @Singleton
    @Provides
    fun providesGitHubApi(): GitHubApi {

        return ApiClientBuilder.createService(GitHubApi::class.java, GITHUB_BASE_URL)

    }

    @Singleton
    @Provides
    fun providesGitHubRepoSyncRequestManager(): SynchronousRequestManager<GitHubRepoResponse> {
        return SynchronousRequestManagerImpl<GitHubRepoResponse>()
    }

    @Singleton
    @Provides
    fun providesPullRequestSyncRequestManager(): SynchronousRequestManager<List<PullRequest>> {
        return SynchronousRequestManagerImpl<List<PullRequest>>()
    }


}