package com.igorvd.githubrepo.domain

import com.igorvd.githubrepo.domain.exceptions.RepositoryException
import kotlinx.coroutines.experimental.Deferred

/**
 * @author Igor Vilela
 * @since 13/10/17
 */

interface Interactor<T, Params> {

    @Throws(RepositoryException::class)
    suspend fun execute(params: Params): Deferred<T>

}