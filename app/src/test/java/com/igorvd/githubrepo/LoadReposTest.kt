package com.igorvd.githubrepo

import com.igorvd.githubrepo.data.repository.GitHubRepoRepository
import com.igorvd.githubrepo.domain.LoadGitHubReposInteractor
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
class LoadReposTest {

    val gitHubRepo = mock(GitHubRepoRepository::class.java)
    lateinit var loadReposInteractor : LoadGitHubReposInteractor

    @Before
    fun setUp() {
        loadReposInteractor = LoadGitHubReposInteractor(gitHubRepository = gitHubRepo)
    }

    @Test
    fun testEmpty() = runBlocking {

        `when`(gitHubRepo.loadRepositories()).thenReturn(ArrayList())

        loadReposInteractor.execute(null)

        Mockito.verify(gitHubRepo).loadRepositories()
        Mockito.verifyNoMoreInteractions(gitHubRepo)

    }
}
