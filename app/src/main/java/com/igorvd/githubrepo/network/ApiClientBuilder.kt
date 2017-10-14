package com.igorvd.githubrepo.network

import com.igorvd.githubrepo.BuildConfig
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit




/**
 * Helper class that creates retrofit clients
 * @author Igor Vilela
 * @since 12/10/17
 */
class ApiClientBuilder {

    companion object {

        private val HEADER_TIMEZONE = "Timezone"

        private lateinit var sRetrofit: Retrofit
        private lateinit var sHttpClientBuilder: OkHttpClient.Builder

        fun <S> createService(serviceClass: Class<S>, baseUrl: String): S {

            sHttpClientBuilder = OkHttpClient.Builder()

            sHttpClientBuilder.addInterceptor ({
                chain : Interceptor.Chain ->

                val original = chain.request()

                // Request customization: add request headers
                val requestBuilder = original.newBuilder()
                        .header(HEADER_TIMEZONE, "America/Sao_Paulo")
                        .method(original.method(), original.body())

                val request = requestBuilder.build()
                chain.proceed(request)
            })

            if (BuildConfig.DEBUG) {

                // Critical part, LogClient must be last one if you have more interceptors
                sHttpClientBuilder.addInterceptor(HttpLoggingInterceptor().getSimpleLogging())

            }

            val client = sHttpClientBuilder
                    .readTimeout(15, TimeUnit.SECONDS)
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .build()
            sRetrofit = getClientBuilder(baseUrl).client(client).build()
            return sRetrofit.create(serviceClass)
        }

        private fun getClientBuilder(baseUrl: String): Retrofit.Builder {
            return Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
        }
    }

}