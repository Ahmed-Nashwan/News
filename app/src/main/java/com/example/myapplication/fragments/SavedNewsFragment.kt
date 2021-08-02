package com.example.myapplication.fragments

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.NewsActivity
import com.example.myapplication.R
import com.example.myapplication.adapters.NewsAdapter
import com.example.myapplication.view_model.NewsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {
    lateinit var newsViewModel: NewsViewModel
    lateinit var adapter: NewsAdapter
    var recyclerView: RecyclerView? = null
    var fab: FloatingActionButton? = null

    // var tv_no_Internet: TextView? = null
    val args: NewsAcrticleFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsViewModel = (activity as NewsActivity).newsViewModel
        fab = view.findViewById(R.id.fab)
        recyclerView = view.findViewById(R.id.rvSavedNews)
        // progressBar = view.findViewById(R.id.paginationProgressBar)
        //  tv_no_Internet = view.findViewById(R.id.tv_no_Internet)


        newsViewModel.getAllNews().observe(viewLifecycleOwner, { articles ->
            adapter.asyncListDiffer.submitList(articles)

        })
        setupAdapter()

        fab?.setOnClickListener {
            newsViewModel.insert(args.article)
            Snackbar.make(view, "Article by author ${args.article.author}", Snackbar.LENGTH_SHORT)
                .show()

        }

        adapter.setOnClickListener {

            val bundle = Bundle().apply {
                putSerializable("article", it)
            }

            findNavController().navigate(
                R.id.action_searchFragment_to_newsAcrticleFragment,
                bundle
            )
        }



    }

    private fun setupAdapter() {

        adapter = NewsAdapter(requireContext())
        //binding?.rvBreakingNews?.adapter = adapter
        //binding?.rvBreakingNews?.layoutManager = LinearLayoutManager(activity)

        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(activity)


    }

}
