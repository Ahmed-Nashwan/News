package com.example.myapplication.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.myapplication.NewsActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentBreakingNewsBinding
import com.example.myapplication.view_model.NewsViewModel

class NewsAcrticleFragment : Fragment(){
    lateinit var newsViewModel: NewsViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsViewModel = (activity as NewsActivity).newsViewModel

    }
}


