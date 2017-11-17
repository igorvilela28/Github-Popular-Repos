package com.igorvd.githubrepo.presentation.pull_requests

import com.igorvd.githubrepo.domain.entities.PullRequest
import com.igorvd.githubrepo.domain.LoadPullRequestsInteractor
import com.igorvd.githubrepo.presentation.AbstractPresenter
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.async
import javax.inject.Inject
import javax.inject.Named

/**
 * @author Igor Vilela
 * @since 12/10/17
 */
class PullRequestsPresenter
@Inject
constructor(
        @Named("network")
        val mLoadPullRequestsInteractor: LoadPullRequestsInteractor,
        var mView : PullRequestsContract.View?) :
        AbstractPresenter(mView), PullRequestsContract.Presenter {

    suspend override fun loadOpenPullRequests(ownerLogin: String, repoName: String,
                                              currentItemsSize: Int) {
        doWorkWithProgress {

            val params = LoadPullRequestsInteractor.Params(ownerLogin, repoName, currentItemsSize)
            val loadPullRequestsJob = async { mLoadPullRequestsInteractor.execute(params) }

             val pullRequests : List<PullRequest> = loadPullRequestsJob.await();

            if(pullRequests.isEmpty()) {
                mView?.onOpenEmpty()
            } else {
                mView?.showOpenPullRequests(pullRequests)
            }
        }

    }

    override fun detachView() {
        super.detachView()
        mView = null
    }

}