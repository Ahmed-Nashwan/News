package com.example.myapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.NewsActivity
import com.example.myapplication.R
import com.example.myapplication.adapters.NewsAdapter
import com.example.myapplication.databinding.FragmentBreakingNewsBinding
import com.example.myapplication.utlis.Resource
import com.example.myapplication.view_model.NewsViewModel

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    lateinit var newsViewModel: NewsViewModel
    lateinit var adapter: NewsAdapter
    var recyclerView: RecyclerView? = null
    var progressBar: ProgressBar? = null
    var tv_no_Internet: TextView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //  binding = FragmentBreakingNewsBinding.inflate(layoutInflater)
        super.onViewCreated(view, savedInstanceState)
        //binding?.root
        recyclerView = view.findViewById(R.id.rvBreakingNews)
        progressBar = view.findViewById(R.id.paginationProgressBar)
        tv_no_Internet = view.findViewById(R.id.tv_no_Internet)
        newsViewModel = (activity as NewsActivity).newsViewModel


        setupAdapter()


        adapter.setOnClickListener {

            val bundle = Bundle().apply {
                putSerializable("article", it)
            }

            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_newsAcrticleFragment,
                bundle
            )


        }

        newsViewModel.breakingNews.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    // binding?.rvBreakingNews?.visibility = INVISIBLE
                    hideProgressbar()
                    response.data?.let { newsRespose ->
                        adapter.asyncListDiffer.submitList(newsRespose.articles.toList()) // differ just take a list not a mutable list
                        val totalPages = newsRespose.totalResults / 20 + 2
                        isLastPage = newsViewModel.breakingNewsPage == totalPages

                        if (isLastPage) {
                            recyclerView?.setPadding(0, 0, 0, 0)
                        }
                    }

                }
                is Resource.Loading -> {
                    //binding?.rvBreakingNews?.visibility = VISIBLE
                    showProgressbar()
                }
                is Resource.Error -> {
                    response.message?.let { error ->
                        tv_no_Internet?.visibility = VISIBLE
                        tv_no_Internet?.text = error
                    }

                }

            }

        })

    }


    var isLoading = false
    var isScrolling = false
    var isLastPage = false

    private fun hideProgressbar() {
        progressBar?.visibility = INVISIBLE
        isLoading = false

    }

    private fun showProgressbar() {
        progressBar?.visibility = VISIBLE
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

            if (shouldPaginate) {
                newsViewModel.getBreakingNews("us")
                isScrolling = false

            }
        }
    }

    private fun setupAdapter() {

        adapter = NewsAdapter(requireContext())
        //binding?.rvBreakingNews?.adapter = adapter
        //binding?.rvBreakingNews?.layoutManager = LinearLayoutManager(activity)

        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        recyclerView?.addOnScrollListener(this.scrollListener)

    }
}




