package com.igorvd.githubrepo.network.requests

import com.igorvd.githubrepo.domain.exceptions.RepositoryException
import retrofit2.Call


/**
 * Makes a synchronous request for some data
 * @author Igor Vilela
 * @since 13/10/17
 */

interface SynchronousRequestManager<T> {

    @Throws(RepositoryException::class)
    fun getResult(call: Call<T>): T

}