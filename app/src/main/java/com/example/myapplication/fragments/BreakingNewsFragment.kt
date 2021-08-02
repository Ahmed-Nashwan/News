package com.example.myapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
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
    val args: NewsAcrticleFragmentArgs by navArgs()

    //  var binding: FragmentBreakingNewsBinding? = null
    var recyclerView: RecyclerView? = null
    var progressBar: ProgressBar? = null
    var tv_no_Internet: TextView? = null

//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        binding = FragmentBreakingNewsBinding.inflate(layoutInflater,container,false)
//  return binding?.root
//    }

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
                    progressBar?.visibility = INVISIBLE
                    response.data?.let { newsRespose ->
                        adapter.asyncListDiffer.submitList(newsRespose.articles)

                    }

                }
                is Resource.Loading -> {
                    //binding?.rvBreakingNews?.visibility = VISIBLE
                    progressBar?.visibility = VISIBLE
                }
                is Resource.Error -> {
                    response.message?.let { error ->
                        Log.d("ttt", "error in get data in lifecycle this is error name : $error")
                    }

                }
                is Resource.NotInternet -> {
                    //  binding?.tvNoInternet?.visibility = VISIBLE
                    tv_no_Internet?.visibility = VISIBLE
                }
            }

        })


    }

    private fun setupAdapter() {

        adapter = NewsAdapter(requireContext())
        //binding?.rvBreakingNews?.adapter = adapter
        //binding?.rvBreakingNews?.layoutManager = LinearLayoutManager(activity)

        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(activity)


    }
}




