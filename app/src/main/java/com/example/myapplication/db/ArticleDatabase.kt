package com.example.myapplication.db

import android.content.Context
import androidx.room.*
import com.example.myapplication.models.Article

@Database(entities = [Article::class], version = 1)
@TypeConverters(Convertes::class)
abstract class ArticleDatabase : RoomDatabase() {


    abstract fun getArticleDoa(): ArticleDao

    companion object {
        @Volatile
        private var instance: ArticleDatabase? = null
        private val look = Any()

         fun instance(context: Context) = instance ?: synchronized(look) {
            instance ?: createDatabase(context).also {
                instance = it
            }

        }

        private fun createDatabase(context: Context)=
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "articleDb.db",
            ).build()


    }

}