package com.igorvd.githubrepo.dependency_injection.domain

import com.igorvd.githubrepo.data.repository.impl.GitHubRepoNetworkRepository
import com.igorvd.githubrepo.data.repository.GitHubRepoRepository
import com.igorvd.githubrepo.domain.LoadGitHubReposInteractor
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * @author Igor Vilela
 * @since 14/10/17
 */

@Module
class GitHubRepoModule {

    @Named("network")
    @Provides
    fun providesGitHubNetworkRepository(gitHubRepoRepository: GitHubRepoNetworkRepository)
            : GitHubRepoRepository {
        return gitHubRepoRepository
    }

    @Named("network")
    @Provides
    fun providesInteractor(@Named("network") gitHubRepoRepository : GitHubRepoRepository) :
            LoadGitHubReposInteractor {
        return LoadGitHubReposInteractor(gitHubRepoRepository)
    }
}