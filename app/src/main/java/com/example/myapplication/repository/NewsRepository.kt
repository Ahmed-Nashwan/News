package com.example.myapplication.repository

import com.example.myapplication.api.RetrofitInstance
import com.example.myapplication.db.ArticleDatabase
import com.example.myapplication.models.Article

class NewsRepository(val db: ArticleDatabase) {

    suspend fun getBrakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(query: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(query, pageNumber)

    suspend fun insert(article:Article) = db.getArticleDoa().insert(article)
    suspend fun delete(article: Article) = db.getArticleDoa().delete(article)
    fun getArticles() = db.getArticleDoa().getAllArticles()




}