package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.ActivityDetailsArticleBinding

class DetailsArticle : AppCompatActivity() {

    var binding:ActivityDetailsArticleBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailsArticleBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)



       // intent.getSerializableExtra("details_article")





    }
}