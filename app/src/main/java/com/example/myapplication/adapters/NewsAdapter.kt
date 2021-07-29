package com.example.myapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemArticlePreviewBinding
import com.example.myapplication.models.Article

class NewsAdapter(var context: Context) :RecyclerView.Adapter<NewsAdapter.MyViewHolder>() {



    private val differCallback = object :DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {

            return oldItem.url ==newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
           return oldItem==newItem
        }
    }

    var asyncListDiffer = AsyncListDiffer(this,differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
      val binding = ItemArticlePreviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       val article = asyncListDiffer.currentList[position]

        holder.bindData(article,context)

      holder.itemView.setOnClickListener{

          onItemClickListener?.let { it(article) }

      }


    }

    override fun getItemCount(): Int {
      return  asyncListDiffer.currentList.size
    }


    class MyViewHolder(private val binding: ItemArticlePreviewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(article: Article,context: Context) {
            Glide.with(context).load(article.urlToImage).placeholder(R.drawable.ic_breaking_news).into(binding.ivArticleImage)
            binding.tvDescription.text = article.description
            binding.tvPublishedAt.text = article.publishedAt
            binding.tvSource.text = article.source.name
            binding.tvTitle.text = article.title

        }





    }


   private  var onItemClickListener:((Article)->Unit)?=null

    fun setOnClickListener(listener:(Article)->Unit){
        onItemClickListener = listener

    }


}