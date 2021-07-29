package com.example.myapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.NewsActivity
import com.example.myapplication.R
import com.example.myapplication.adapters.NewsAdapter
import com.example.myapplication.utlis.Resource
import com.example.myapplication.view_model.NewsViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment(R.layout.fragment_search_news){
    lateinit var viewModel: NewsViewModel
    lateinit var adapter: NewsAdapter
    // var binding:FragmentBreakingNewsBinding? = null
    var recyclerView: RecyclerView? = null
    var progressBar: ProgressBar? = null
    var tv_no_Internet: TextView? = null
    var et_search: EditText? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rvSearchNews)
        progressBar = view.findViewById(R.id.paginationProgressBarSearch)
        tv_no_Internet = view.findViewById(R.id.tv_no_InternetSearch)
        et_search = view.findViewById(R.id.etSearch)
        viewModel = (activity as NewsActivity).newsViewModel


        setupAdapter()

        var job:Job?=null
        et_search?.addTextChangedListener { editable->
        job?.cancel()

            job = MainScope().launch {
                delay(500L)
                editable?.let {
                    viewModel.searchNews(editable.toString())
                }
            }
        }

        viewModel.searchNews.observe(viewLifecycleOwner,  {response->
            when(response){
                is Resource.Success ->{
                    // binding?.rvBreakingNews.visibility = INVISIBLE
                    progressBar?.visibility = View.INVISIBLE
                    response.data?.let { newsRespose->
                        adapter.asyncListDiffer.submitList(newsRespose.articles)

                    }

                }
                is Resource.Loading->{
                    // binding?.rvBreakingNews.visibility = VISIBLE
                    progressBar?.visibility = View.VISIBLE
                }
                is Resource.Error->{
                    response.message?.let { error->
                        Log.d("ttt","error in get data in lifecycle this is error name : $error")
                    }

                }
                is Resource.NotInternet->{
                    tv_no_Internet?.visibility = View.VISIBLE
                }
            }

        })


    }

    private fun setupAdapter(){

        adapter = NewsAdapter(requireContext())
        // binding?.rvBreakingNews.adapter = adapter
        // binding?.rvBreakingNews.layoutManager = LinearLayoutManager(activity)

        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(activity)



    }
}


