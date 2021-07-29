package com.example.myapplication.api

import com.example.myapplication.models.NewsResponse
import com.example.myapplication.utlis.Contacts.Companion.APK_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country") countryCode: String = "us",
        @Query("page") pageNumber: Int = 1,
        @Query("apiKey") apiKey: String = APK_KEY
    ): Response<NewsResponse>



    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q") query: String,
        @Query("page") pageNumber: Int = 1,
        @Query("apiKey") apiKey: String = APK_KEY
    ): Response<NewsResponse>
}