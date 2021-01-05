package com.sournary.architecturecomponent.data.api

import com.sournary.architecturecomponent.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * The class help us define useful functions with network.
 */
object NetworkManager {

    private const val TIME_OUT = 10L
    private const val API_KEY_PARAM = "api_key"

    fun createMovieDbApi(): MovieDbApi = createRetrofit().create(MovieDbApi::class.java)

    private fun createRetrofit(): Retrofit {
        val builder = Retrofit.Builder().apply {
            baseUrl(BuildConfig.BASE_URL)
            client(createOkHttpClient())
            addConverterFactory(GsonConverterFactory.create())
        }
        return builder.build()
    }

    private fun createOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder().apply {
            connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            readTimeout(TIME_OUT, TimeUnit.SECONDS)
            writeTimeout(TIME_OUT, TimeUnit.SECONDS)
            addInterceptor(createHeaderInterceptor())
            addInterceptor(createLoggingInterceptor())
        }
        return builder.build()
    }

    private fun createHeaderInterceptor(): Interceptor = Interceptor.invoke { chain ->
        val currentRequest = chain.request()
        val newUrl = currentRequest.url
            .newBuilder()
            .addQueryParameter(API_KEY_PARAM, BuildConfig.API_KEY)
            .build()
        val newRequest = currentRequest.newBuilder().apply {
            url(newUrl)
        }
        chain.proceed(newRequest.build())
    }

    private fun createLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = when (BuildConfig.DEBUG) {
            true -> HttpLoggingInterceptor.Level.BODY
            else -> HttpLoggingInterceptor.Level.NONE
        }
    }

}
