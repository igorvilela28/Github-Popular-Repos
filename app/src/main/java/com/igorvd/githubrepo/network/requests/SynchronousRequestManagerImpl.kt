package com.igorvd.githubrepo.network.requests

import com.igorvd.githubrepo.domain.exceptions.BadRequestRepositoryException
import com.igorvd.githubrepo.domain.exceptions.IORepositoryException
import timber.log.Timber
import com.igorvd.githubrepo.domain.exceptions.RepositoryException
import retrofit2.Call
import retrofit2.Response
import java.io.IOException


/**
 * @author Igor Vilela
 * @since 13/10/17
 */
class SynchronousRequestManagerImpl<T> : SynchronousRequestManager<T> {

    @Throws(RepositoryException::class)
    override fun getResult(call: Call<T>): T {

        try {

            val response: Response<T> = call.execute()
            return parseResponse(call, response)

        } catch (e: IOException) {

            val url = call.request().url().toString()
            Timber.d("IOError in call, url: %s", url)
            throw IORepositoryException(url)

        } catch (e: Exception) {

            //we are only retrowing it to be more clear when debugging
            Timber.e(e, "Exception %s on SyncRequestManagerImpl", e.javaClass.simpleName)
            throw e
        }

    }

    @Throws(RepositoryException::class)
    private fun parseResponse(call: Call<T>, response: Response<T>): T {

        if (response.isSuccessful) {

            Timber.d("object created/updated on cloud")

            return response.body()!!

        } else {

            //TODO: improve http exceptions handling

            val url = call.request().url().toString()

            when(response.code()) {

                in 300..500 -> throw BadRequestRepositoryException("bad request in $url")

            }

            throw BadRequestRepositoryException("bad request in $url")

        }
    }
}