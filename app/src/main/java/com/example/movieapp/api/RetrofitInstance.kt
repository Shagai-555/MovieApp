package com.example.movieapp.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://api.themoviedb.org/3/"

    val api: TMDbApi by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIzZDhhY2Q3YmEyN2QxZjJhNTFkYWJjYzI0N2QwMjNmMSIsIm5iZiI6MTczMTI0MTUxMS42MjkzODc2LCJzdWIiOiI2NzFjOTFjMDRiZTE1NDY5ZTcwZGQwNTMiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.l3C7YVe0qBfyu_j7VvyNAWqR_x4O0_uA21pwqZtYch0")
                    .build()
                chain.proceed(request)
            }
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TMDbApi::class.java)
    }
}
