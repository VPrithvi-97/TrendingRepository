package com.example.githubrepos.ui.repos

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.githubrepos.network.ApiClient
import com.example.githubrepos.network.ApiService

class ReposRepository {

    private val apiServices by lazy { ApiClient.client.create(ApiService::class.java) }
    fun getSearchResults(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PagingRepos(apiServices, query) }
        ).liveData
}