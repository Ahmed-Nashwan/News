package com.example.myapplication.view_model

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.models.NewsResponse
import com.example.myapplication.repository.NewsRepository
import com.example.myapplication.utlis.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(val newsRepository:NewsRepository,val context: Context):ViewModel() {

    val breakingNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val breakingNewsPage = 1
    val searchNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val searchgNewsPage = 1

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode:String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
      if(isOnline(context =context)){
          val response = newsRepository.getBrakingNews(countryCode,breakingNewsPage)
          breakingNews.postValue(handleBreakingNewsRespose(response))
      }else{
          breakingNews.postValue(Resource.NotInternet())
      }

    }

    fun searchNews(query:String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response = newsRepository.searchNews(query,searchgNewsPage)
        searchNews.postValue(handleSearchNewsResponse(response))
    }

    private fun handleBreakingNewsRespose(response: Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful){

            response.body()?.let {resultRes->

                return Resource.Success(resultRes)
            }

        }
        return Resource.Error(message = response.message())

    }


    private fun handleSearchNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful){

            response.body()?.let {resultRes->

                return Resource.Success(resultRes)
            }

        }
        return Resource.Error(message = response.message())

    }



    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
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
        }
        return false
    }

}