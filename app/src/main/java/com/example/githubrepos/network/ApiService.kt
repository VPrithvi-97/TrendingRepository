package com.example.githubrepos.network

import com.example.githubrepos.model.Repositories
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("search/repositories?sort=stars")
    suspend fun getTrendingRepos(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): Repositories
}