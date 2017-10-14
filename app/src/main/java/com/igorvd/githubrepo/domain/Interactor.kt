package com.igorvd.githubrepo.domain

import com.igorvd.githubrepo.data.GitHubRepo
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.run

/**
 * @author Igor Vilela
 * @since 13/10/17
 */
interface Interactor<T> {

    suspend fun execute(): Deferred<T>

}