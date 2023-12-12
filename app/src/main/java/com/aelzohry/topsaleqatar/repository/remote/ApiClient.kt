package com.aelzohry.topsaleqatar.repository.remote

import android.util.Log
import com.aelzohry.topsaleqatar.App
import com.aelzohry.topsaleqatar.BuildConfig
import com.aelzohry.topsaleqatar.helper.Constants
import com.aelzohry.topsaleqatar.utils.Utils
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

//    private val shared = SharedPref()

    fun api(): WebService {
        val httpClient = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(interceptor)
        }

        val client = httpClient
            .connectTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .addInterceptor(HeaderInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create<WebService>(WebService::class.java)
    }


    /**
     * Provides the okHttp client for networking
     *
     * @param cache the okHttp cache
     * @param loggingInterceptor the okHttp logging interceptor
     * @param onlineInterceptor the interceptor to be used in case of online network
     * @param offlineInterceptor the interceptor to be used in case of offline network
     * @return okHttp client instance
     */
    private fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .cache(setupHttpCache())
            .addNetworkInterceptor(setupHttpOnlineInterceptor())
            .addInterceptor(setupHttpOfflineInterceptor())
//            .addInterceptor(setupHttpLoggingInterceptor())
            .addInterceptor(setupHeadersInterceptor())
            .addNetworkInterceptor(setupHeadersInterceptor())
            .connectTimeout(300, TimeUnit.SECONDS) // connect timeout
            .readTimeout(300, TimeUnit.SECONDS)
            .build()// socket timeout

    /**
     * Provide Http Interceptor to update cache if network connected and add Cache-Control to responseBody
     * header if Cache not supported in the server
     *
     * @return  Http Interceptor instance
     */
    private fun setupHeadersInterceptor(): Interceptor = Interceptor { chain ->
        var request = chain.request()

//        val pref = SharedPref()
        request = request.newBuilder()
            .header("Accept", "application/json")
//            .header(
//                "Authorization", /*shared.token*/
//                "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9pZnJlc2guc2FcL2FwaVwvbG9naW4iLCJpYXQiOjE2MDc5NzY3OTEsImV4cCI6MTcxNTk5NzY3OTEsIm5iZiI6MTYwNzk3Njc5MSwianRpIjoiQUFlRGttOERDelNUcmJQdiIsInN1YiI6MiwicHJ2IjoiYmI2NWQ5YjhmYmYwZGE5ODI3YzhlZDIzMWQ5YzU0YzgxN2YwZmJiMiJ9.6wSSeBkVSBkz3kSpZ2Ez1X7NmDVXRoyc1heORRCvxQE"
//            )

//            .header("Authorization", /*shared.token*/  if (pref.loginMode) "Bearer ${pref.userInfo?.access_token}" else "")
//            .header("lang", shared.lang.lang)
//            .header("FbToken", pref.devToken)
            .build()
        chain.proceed(request)
    }

    /**
     * Provide Http Interceptor to get data from cache if network disconnected
     *
     * @param context the application context to e used in check the network state
     * @return  Http Interceptor instance
     */
    private fun setupHttpOfflineInterceptor(): Interceptor = Interceptor { chain ->
        var request = chain.request()
        if (Utils.isNetworkConnected()) {

            request = request.newBuilder()
                .header(
                    "Cache-Control",
                    "public, only-if-cached, max-stale=${Constants.CACHE_MAX_STALE}"
                )
                .build()
        }
        chain.proceed(request)
    }

    /**
     * Provide Http Interceptor to update cache if network connected and add Cache-Control to responseBody
     * header if Cache not supported in the server
     *
     * @param context the application context to e used in check the network state
     * @return  Http Interceptor instance
     */
    private fun setupHttpOnlineInterceptor(): Interceptor = Interceptor { chain ->
        val response = chain.proceed(chain.request())

        val headers = response.header("Cache-Control")
        if (!Utils.isNetworkConnected() && (headers == null ||
                    headers.contains("no-store") || headers.contains("must-revalidate") ||
                    headers.contains("no-cache") || headers.contains("max-age=0"))
        ) {

            response.newBuilder()
                .header("Cache-Control", "public, max-age=${Constants.CACHE_MAX_AGE}")
                .build()
        } else
            response
    }

    /**
     * Provides okHttp cache to be used in okHttp client
     *
     * @return the okHttp cache
     */
    private fun setupHttpCache() = Cache(App.context.filesDir, Constants.CACHE_SIZE.toLong())


    /**
     * provide Http Interceptor to be used in logging networking
     *
     * @return an instance of Http Interceptor with custom logging
     */

//    private fun setupHttpLoggingInterceptor(): HttpLoggingInterceptor {
//        val logger = HttpLoggingInterceptor.Logger { s -> Log.i(Constants.NETWORKING_LOG, s) }
//        val loggingInterceptor = HttpLoggingInterceptor(logger)
//        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

//        return loggingInterceptor
//    }
}