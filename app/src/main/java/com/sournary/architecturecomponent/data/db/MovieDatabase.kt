package com.sournary.architecturecomponent.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sournary.architecturecomponent.model.Genre
import com.sournary.architecturecomponent.model.Movie
import com.sournary.architecturecomponent.util.Constant.DB_NAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *
 */
@Database(entities = [Genre::class, Movie::class], version = 1, exportSchema = true)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun getGenreDao(): GenreDao

    abstract fun getMovieDao(): MovieDao

    companion object {

        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getInstance(context: Context): MovieDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, MovieDatabase::class.java, DB_NAME)
                // prepopulate the database after onCreate was called
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.Default).launch {
                            getInstance(context).getGenreDao().insert(Genre.SAVED_GENRES)
                        }
                    }
                })
                .build()
    }
}
