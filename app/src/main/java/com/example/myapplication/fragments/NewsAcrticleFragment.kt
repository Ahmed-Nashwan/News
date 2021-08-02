package com.example.myapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.example.myapplication.NewsActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentBreakingNewsBinding
import com.example.myapplication.view_model.NewsViewModel

class NewsAcrticleFragment : Fragment(R.layout.fragment_article){
    lateinit var newsViewModel: NewsViewModel
    val args:NewsAcrticleFragmentArgs by navArgs()
    var webView:WebView? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsViewModel = (activity as NewsActivity).newsViewModel
        webView = view.findViewById(R.id.webView)


      val article =   args.article
        Log.d("ttt",article.url)
        Log.d("ttt","hello world")

        webView?.apply {
         webViewClient =    WebViewClient()
            loadUrl(article.url)
        }



    }
}


