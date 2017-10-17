package com.igorvd.githubrepo.presentation.pull_requests

import com.igorvd.githubrepo.data.PullRequest
import com.igorvd.githubrepo.domain.LoadPullRequestsInteractor
import com.igorvd.githubrepo.domain.exceptions.IORepositoryException
import com.igorvd.githubrepo.domain.exceptions.RepositoryException
import com.igorvd.githubrepo.utils.extensions.throwOrLog
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
        PullRequestsContract.Presenter {

    suspend override fun loadOpenPullRequests(ownerLogin: String, repoName: String,
                                              currentItemsSize: Int) {
        doWorkWithProgress {

            val params = LoadPullRequestsInteractor.Params(ownerLogin, repoName, currentItemsSize)
            val pullRequests: List<PullRequest> = mLoadPullRequestsInteractor.execute(params).await()

            if(pullRequests.isEmpty()) {
                mView?.blockRetrieveOpen()
            } else {
                mView?.showOpenPullRequests(pullRequests)
            }
        }

    }

    suspend override fun loadClosedPullRequests(ownerLogin: String, repoName: String,
                                                currentItemsSize: Int) {

    }

    override fun detachView() {

        mView = null
    }

    private suspend fun doWorkWithProgress(work: suspend () -> Unit) {

        mView?.showProgress()
        try {
            work()
        } catch(e: IORepositoryException) {
            mView?.showError()
        } catch (e: RepositoryException) {
            mView?.showError()
        } catch (e: Exception) {
            e.throwOrLog()
        } finally {
            mView?.hideProgress()
        }
    }

}