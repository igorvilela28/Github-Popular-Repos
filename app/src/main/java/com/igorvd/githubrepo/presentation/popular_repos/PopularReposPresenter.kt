package com.igorvd.githubrepo.presentation.popular_repos

import com.igorvd.githubrepo.domain.LoadGitHubReposInteractor
import com.igorvd.githubrepo.domain.exceptions.IORepositoryException
import com.igorvd.githubrepo.domain.exceptions.RepositoryException
import com.igorvd.githubrepo.presentation.AbstractPresenter
import com.igorvd.githubrepo.utils.extensions.throwOrLog
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
        @Named("network")
        val mLoadGitHubReposInteractor: LoadGitHubReposInteractor,
        var mView : PopularReposContract.View?) :
        AbstractPresenter(mView) , PopularReposContract.Presenter  {

    override suspend fun loadRepositories(currentItemsSize: Int) {

        doWorkWithProgress({

            val params = LoadGitHubReposInteractor.Params(currentItemsSize)
            val repos = mLoadGitHubReposInteractor.execute(params).await()
            mView?.showRepositories(repos)
        })
    }

    override fun detachView() {

        super.detachView()
        mView = null

    }
}