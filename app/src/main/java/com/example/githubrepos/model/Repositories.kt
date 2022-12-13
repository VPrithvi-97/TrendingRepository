package com.example.githubrepos.model

import com.example.githubrepos.model.Item

data class Repositories(
    val incomplete_results: Boolean,
    val items: List<Item>,
    val total_count: Int
)