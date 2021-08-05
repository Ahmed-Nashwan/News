package com.example.myapplication.view_model

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.NewsApplication
import com.example.myapplication.models.Article
import com.example.myapplication.models.NewsResponse
import com.example.myapplication.repository.NewsRepository
import com.example.myapplication.utlis.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(val newsRepository: NewsRepository, val app: Application) :
    AndroidViewModel(app) {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null
    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null


    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
      safeBreakingNewsCall(countryCode)
    }

    fun searchNews(query: String) = viewModelScope.launch {
      safeSearchNewsCall(query)
    }


    fun insert(article: Article) = viewModelScope.launch {
        newsRepository.insert(article)
    }

    fun delete(article: Article) = viewModelScope.launch {
        newsRepository.delete(article)
    }

    fun getAllNews() = newsRepository.getArticles()



    private fun handleBreakingNewsRespose(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {

            response.body()?.let { resultRes ->
                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultRes
                } else {
                    val oldData = breakingNewsResponse?.articles
                    val newData = resultRes.articles
                    oldData?.addAll(newData)


                }
                return Resource.Success(breakingNewsResponse ?: resultRes)
            }

        }
        return Resource.Error(message = response.message())

    }


    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {

            response.body()?.let { resultRes ->
                searchNewsPage++
                if (searchNewsResponse == null) {
                    searchNewsResponse = resultRes
                } else {
                    val oldData = searchNewsResponse?.articles
                    val newData = resultRes.articles
                    oldData?.addAll(newData)


                }
                return Resource.Success(searchNewsResponse ?: resultRes)
            }

        }
        return Resource.Error(message = response.message())

    }

    private suspend fun safeBreakingNewsCall(countryCode: String) {
        breakingNews.postValue(Resource.Loading())
        try {
            if (isOnline()) {
                val response = newsRepository.getBrakingNews(countryCode, breakingNewsPage)
                breakingNews.postValue(handleBreakingNewsRespose(response))
            } else {
                breakingNews.postValue(Resource.Error(message = "No Internet connection"))

            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> breakingNews.postValue(Resource.Error(message = "no Internet connection"))
                else -> breakingNews.postValue(Resource.Error(message = "Error converter"))
            }

        }

    }

    private suspend fun safeSearchNewsCall(searchQuery: String) {
        searchNews.postValue(Resource.Loading())
        try {
            if (isOnline()) {
                val response = newsRepository.searchNews(searchQuery, searchNewsPage)
                searchNews.postValue(handleSearchNewsResponse(response))
            } else {
                searchNews.postValue(Resource.Error(message = "No Internet connection"))

            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchNews.postValue(Resource.Error(message = "no Internet connection"))
                else -> searchNews.postValue(Resource.Error(message = "Error converter"))
            }

        }

    }


    fun isOnline(): Boolean {
        val connectivityManager =
            app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            } else {
                TODO("VERSION.SDK_INT < M")
            }
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

}