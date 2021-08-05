package com.example.myapplication

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.api.RetrofitInstance
import com.example.myapplication.databinding.ActivityNewsBinding
import com.example.myapplication.db.ArticleDatabase
import com.example.myapplication.repository.NewsRepository
import com.example.myapplication.view_model.NewsViewModel
import com.example.myapplication.view_model.NewsViewModelProviderFactory
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView

class NewsActivity : AppCompatActivity() {

    // var binding :ActivityNewsBinding? = null

    lateinit var newsViewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        // binding = ActivityNewsBinding.inflate(layoutInflater)
        val rebo = NewsRepository(ArticleDatabase.instance(this))
        val providerFactory = NewsViewModelProviderFactory(rebo, application)
        newsViewModel = ViewModelProvider(this, providerFactory).get(NewsViewModel::class.java)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)



        val bottom_nav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        val navController = findNavController(R.id.fragment)
        bottom_nav.setupWithNavController(navController)




    }
}