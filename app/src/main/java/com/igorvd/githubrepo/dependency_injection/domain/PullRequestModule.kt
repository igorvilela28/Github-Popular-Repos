package com.igorvd.githubrepo.dependency_injection.domain

import com.igorvd.githubrepo.data.repository.impl.PullRequestsNetworkRepository
import com.igorvd.githubrepo.data.repository.PullRequestsRepository
import com.igorvd.githubrepo.domain.LoadPullRequestsInteractor
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * @author Igor Vilela
 * @since 14/10/17
 */

@Module
class PullRequestModule {

    @Named("network")
    @Provides
    fun providesPullRequestNetworkRepository(pullRequestRepository: PullRequestsNetworkRepository)
            : PullRequestsRepository {
        return pullRequestRepository
    }

    @Named("network")
    @Provides
    fun providesInteractor(@Named("network") pullRequestRepository : PullRequestsRepository) :
            LoadPullRequestsInteractor {
        return LoadPullRequestsInteractor(pullRequestRepository)
    }
}