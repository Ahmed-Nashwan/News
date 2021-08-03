package com.example.myapplication.fragments

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.NewsActivity
import com.example.myapplication.R
import com.example.myapplication.adapters.NewsAdapter
import com.example.myapplication.models.Article
import com.example.myapplication.view_model.NewsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {
    lateinit var newsViewModel: NewsViewModel
    lateinit var adapter: NewsAdapter
    var recyclerView: RecyclerView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsViewModel = (activity as NewsActivity).newsViewModel
        recyclerView = view.findViewById(R.id.rvSavedNews)


        newsViewModel.getAllNews().observe(viewLifecycleOwner, { articles ->
            adapter.asyncListDiffer.submitList(articles)

        })
        setupAdapter()


        val itemTouchHelperCallBack =
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                    val a: Article =
                        adapter.asyncListDiffer.currentList[viewHolder.absoluteAdapterPosition]
                    newsViewModel.delete(a)

                    Snackbar.make(view, "Article by author ${a.author} deleted", Snackbar.LENGTH_SHORT)
                        .apply {
                            setAction("Undo"){
                                newsViewModel.insert(a)
                            }
                            show()

                        }


                }
            }

        ItemTouchHelper(itemTouchHelperCallBack).apply {
            attachToRecyclerView(recyclerView)
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
