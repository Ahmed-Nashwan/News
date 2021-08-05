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
import com.example.myapplication.models.Article
import com.example.myapplication.view_model.NewsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class NewsAcrticleFragment : Fragment(R.layout.fragment_article){
    lateinit var newsViewModel: NewsViewModel
    val args:NewsAcrticleFragmentArgs by navArgs()
    var webView:WebView? = null
    var fab: FloatingActionButton? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsViewModel = (activity as NewsActivity).newsViewModel
         fab = view.findViewById(R.id.fab)
        webView = view.findViewById(R.id.webView)


      val article:Article =   args.article

        fab?.setOnClickListener {
            newsViewModel.insert(article)
            Snackbar.make(view, "Article by author ${args.article.author}", Snackbar.LENGTH_SHORT)
                .show()
        }

        webView?.apply {
         webViewClient =    WebViewClient()
            article.url?.let { loadUrl(it) }
        }



    }
}


