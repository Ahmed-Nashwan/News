package com.example.myapplication.view_model

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.repository.NewsRepository

class NewsViewModelProviderFactory(val newsRepo: NewsRepository,val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(newsRepo,app = app) as T

    }

}