package com.example.githubrepos.ui.repos

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn

class ReposViewModel: ViewModel(){

    private val repo = ReposRepository()
    private val currentQuery = MutableLiveData(DEFAULT_QUERY)

    val repos = currentQuery.switchMap { queryString ->
        repo.getSearchResults(queryString).cachedIn(viewModelScope)
    }

    fun searchRepos(query: String) {
        currentQuery.value = query
    }

    companion object {
        private const val DEFAULT_QUERY = "language:Kotlin"
    }
}