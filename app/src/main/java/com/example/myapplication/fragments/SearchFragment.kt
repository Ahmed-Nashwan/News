package com.example.myapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
    val args:NewsAcrticleFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rvSearchNews)
        progressBar = view.findViewById(R.id.paginationProgressBarSearch)
        tv_no_Internet = view.findViewById(R.id.tv_no_InternetSearch)
        et_search = view.findViewById(R.id.etSearch)
        viewModel = (activity as NewsActivity).newsViewModel


        setupAdapter()

        adapter.setOnClickListener {

            val bundle = Bundle().apply {
                putSerializable("article", it)
            }

            findNavController().navigate(
                R.id.action_searchFragment_to_newsAcrticleFragment,
                bundle
            )
        }

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
                 hideProgressbar()
                    response.data?.let { newsRespose->
                        adapter.asyncListDiffer.submitList(newsRespose.articles.toList())
                        val totalPages = newsRespose.totalResults/20+2
                        isLastPage = viewModel.searchNewsPage==totalPages
                        if (isLastPage) {
                            recyclerView?.setPadding(0, 0, 0, 0)
                        }
                    }

                }
                is Resource.Loading->{
                    // binding?.rvBreakingNews.visibility = VISIBLE
                 showProgressbar()
                }
                is Resource.Error->{
                    response.message?.let { error->
                        tv_no_Internet?.visibility = View.VISIBLE
                        tv_no_Internet?.text = error
                    }

                }

            }

        })


    }


    var isLoading = false
    var isScrolling = false
    var isLastPage = false

    private fun hideProgressbar(){
        progressBar?.visibility = View.INVISIBLE
        isLoading = false

    }

    private fun showProgressbar(){
        progressBar?.visibility = View.VISIBLE
        isLoading = true

    }

    var scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount // return number of elements that showed
            val totalItemCount =
                layoutManager.itemCount // return number of elements that give it to adapter

            val isNotLoadingAndNotInTheLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAlBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= 20
            val shouldPaginate =
                isNotLoadingAndNotInTheLastPage && isAtLastItem && isNotAlBeginning && isTotalMoreThanVisible && isScrolling

            if(shouldPaginate){
                viewModel.searchNews(et_search?.text.toString())
                isScrolling = false

            }
        }
    }


    private fun setupAdapter(){

        adapter = NewsAdapter(requireContext())
        // binding?.rvBreakingNews.adapter = adapter
        // binding?.rvBreakingNews.layoutManager = LinearLayoutManager(activity)

        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        recyclerView?.addOnScrollListener(this.scrollListener)


    }
}


