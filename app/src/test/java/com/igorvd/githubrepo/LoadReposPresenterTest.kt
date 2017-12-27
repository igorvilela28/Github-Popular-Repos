package com.igorvd.githubrepo

import com.igorvd.githubrepo.data.repository.GitHubRepoRepository
import com.igorvd.githubrepo.domain.LoadGitHubReposInteractor
import com.igorvd.githubrepo.domain.entities.GitHubRepo
import com.igorvd.githubrepo.presentation.popular_repos.PopularReposContract
import com.igorvd.githubrepo.presentation.popular_repos.PopularReposPresenter
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class LoadReposPresenterTest {

    val interactor = mock(LoadGitHubReposInteractor::class.java)
    val view = mock(PopularReposContract.View::class.java)
    lateinit var presenter: PopularReposContract.Presenter
    @Before
    fun setUp() {
        presenter = PopularReposPresenter(interactor, view)
    }

    @Test
    fun testEmpty() = runBlocking {

        val list : List<GitHubRepo> = ArrayList()
        `when`(interactor.execute(null)).thenReturn(list)

        presenter.loadRepositories()

        Mockito.verify(interactor).execute(null)
        Mockito.verifyNoMoreInteractions(interactor)

        Mockito.verify(view.showProgress())
        Mockito.verify(view.showRepositories(list))
        Mockito.verify(view.hideProgress())
        Mockito.verifyZeroInteractions(view.showError())

    }
}
