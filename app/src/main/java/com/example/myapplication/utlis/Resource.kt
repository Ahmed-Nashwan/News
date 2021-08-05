package com.example.myapplication.utlis

sealed class Resource<T>(val data: T? = null, val message: String? = null) {

    class Success<T>(data:T) : Resource<T>(data)
    class Error<T>(data:T?=null,message:String?=null):Resource<T>(data,message)
    class Loading<T>:Resource<T>()


}