package com.sournary.architecturecomponent.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sournary.architecturecomponent.model.Genre
import com.sournary.architecturecomponent.model.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *
 */
@Database(entities = [Genre::class, Movie::class], version = 1, exportSchema = true)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun getGenre(): GenreDao

    abstract fun getMovieDao(): MovieDao

    companion object {

        // Singleton prevents multiple instances of database opening at the same time (multiple threads).
        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getInstance(context: Context): MovieDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(context, MovieDatabase::class.java, "movie_db")
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.Default).launch {
                                getInstance(context).getGenre().insert(Genre.SAVED_GENRES)
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                return instance
            }
        }

    }

}
