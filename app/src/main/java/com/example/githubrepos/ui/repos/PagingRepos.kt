package com.example.githubrepos.ui.repos

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.githubrepos.model.Item
import com.example.githubrepos.network.ApiService
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class PagingRepos(
    private val apiService: ApiService,
    private val query: String
) : PagingSource<Int, Item>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            val response = apiService.getTrendingRepos(query, position, params.loadSize)
            val repos = response.items

            LoadResult.Page(
                data = repos,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (repos.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }

    }
}