package com.example.myapplication.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.repository.NewsRepository

class NewsViewModelProviderFactory(val newsRepo: NewsRepository,val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(newsRepo,context = context) as T
    }

}