package com.example.myapplication.models

import com.example.myapplication.models.Article

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)