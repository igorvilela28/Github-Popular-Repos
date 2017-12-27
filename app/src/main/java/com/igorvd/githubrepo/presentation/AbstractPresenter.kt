package com.igorvd.githubrepo.presentation

import com.igorvd.githubrepo.domain.exceptions.IORepositoryException
import com.igorvd.githubrepo.domain.exceptions.RepositoryException
import com.igorvd.githubrepo.utils.extensions.throwOrLog

/**
 * C
 * @author Igor Vilela
 * @since 17/10/17
 */
abstract class AbstractPresenter(var mBaseView: BaseView?) : BasePresenter {

    override fun detachView() {
        mBaseView = null
    }

    /**
     * This method should be used when we the presenter is asked to do some long running task.
     * Because we're running a long task, the user should see a progress indicator, and when the
     * job completes we need to remove the progress. This method is useful to avoid duplication of
     * this process
     *
     * param[work] a function to be executed between the [BaseView.showProgress] and the
     * [BaseView.hideProgress] or [BaseView.showError] methods
     */
    protected suspend fun doWorkWithProgress(work: suspend () -> Unit) {

        mBaseView?.showProgress()
        try {
            work()
        } catch(e: IORepositoryException) {
            mBaseView?.showError()
        } catch (e: RepositoryException) {
            mBaseView?.showError()
        } catch (e: Exception) {
            e.throwOrLog()
        } finally {
            mBaseView?.hideProgress()
        }
    }
}