package com.igorvd.githubrepo.presentation.popular_repos

import com.igorvd.githubrepo.domain.LoadGitHubReposInteractor
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named

/**
 * @author Igor Vilela
 * @since 12/10/17
 */
class PopularReposPresenter
@Inject
constructor(
        @Named("network") val mLoadGitHubReposInteractor: LoadGitHubReposInteractor,
        val mView : PopularReposContract.View?) :
        PopularReposContract.Presenter  {

    override suspend fun loadRepositories(language: String?, sort: String, order: String, page: Int) {

        doWorkWithProgress({
            val repos = mLoadGitHubReposInteractor.execute().await()
            mView?.showRepositories(repos)
        })
    }

    private suspend fun doWorkWithProgress(work: suspend () -> Unit) {

        mView?.showProgress()
        try {
            work.invoke()
        } catch(e: IOException) {
            mView?.showError()
        } catch (e: Exception) {
            mView?.showError()
        } finally {
            mView?.hideProgress()
        }
    }

}