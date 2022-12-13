package com.example.githubrepos.network

import android.os.Bundle
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    companion object {
        private var loggingInterceptor: HttpLoggingInterceptor? = null

        val baseURL: String = "https://api.github.com/"
        var retofit: Retrofit? = null
        private var clientBuilder: OkHttpClient.Builder? = null
        val client: Retrofit
            get() {
                if (retofit == null) {
                    val client = OkHttpClient.Builder()
                    client.followRedirects(false)
                    client.addInterceptor { chain ->
                        var request: Request = chain.request()
                        var url: String = request.url.toString()
                        try {
                            url = url.replace(baseURL, "").split("?")[0].replace("/", "_")
                        } catch (e: Exception) {
                        }
                        var response = chain.proceed(request)
                        try {
                            if (response.code == 200 || response.code == 201) {
                            } else if (response.code == 307 || response.code == 301) {
                                Log.d(
                                    "RedirectInterceptor",
                                    "Location Header ${response.header("Location")}"
                                )
                                request = request.newBuilder()
                                    .url(response.header("Location") ?: "")
                                    .build()
                                response = chain.proceed(request)
                            } else {
                                val bundle: Bundle = Bundle()
                                bundle.putInt("status", response.code)
                                bundle.putString("message", response.body.toString())
                            }
                        } catch (e: Exception) {
                            Log.e("APIClient", "Exception $e")
                        }
                        response
                    }.connectTimeout(10000, TimeUnit.SECONDS).readTimeout(10000, TimeUnit.SECONDS)
                    loggingInterceptor = HttpLoggingInterceptor()
                    loggingInterceptor!!.level = HttpLoggingInterceptor.Level.BODY
                    // client.addInterceptor(CustomInterceptor())
                    client.addInterceptor(loggingInterceptor!!)

//                    if (BuildConfig.DEBUG) { client.addNetworkInterceptor( StethoInterceptor ()) }

                    retofit = Retrofit.Builder()
                        .baseUrl(baseURL)
                        .client(client.build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                }
                return retofit!!
            }


    }
}