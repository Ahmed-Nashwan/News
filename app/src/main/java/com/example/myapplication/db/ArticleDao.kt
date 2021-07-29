package com.example.myapplication.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myapplication.models.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article):Long // insert and update in the same time

    @Delete
    suspend fun delete(article: Article)

    @Query("SELECT * FROM article")
    fun getAllArticles():LiveData<Article>


}