package com.sournary.architecturecomponent.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sournary.architecturecomponent.data.api.MovieDbApi
import com.sournary.architecturecomponent.data.api.NetworkManager
import com.sournary.architecturecomponent.data.db.GenreDao
import com.sournary.architecturecomponent.data.db.MovieDao
import com.sournary.architecturecomponent.data.db.MovieDatabase
import com.sournary.architecturecomponent.data.pref.AppPreference
import com.sournary.architecturecomponent.data.pref.DefaultAppPreference
import com.sournary.architecturecomponent.model.Genre
import com.sournary.architecturecomponent.util.Constant.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

/**
 * The files defines modules that work with local app such as database, preference...
 */
@InstallIn(ApplicationComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideMovieDatabase(@ApplicationContext context: Context): MovieDatabase =
        MovieDatabase.getInstance(context)

    @Singleton
    @Provides
    fun provideMovieDao(database: MovieDatabase): MovieDao = database.getMovieDao()

    @Singleton
    @Provides
    fun provideGenreDao(database: MovieDatabase): GenreDao = database.getGenreDao()

    @Singleton
    @Provides
    fun provideMovieApi(): MovieDbApi = NetworkManager.createMovieDbApi()

    @Singleton
    @Provides
    fun provideAppPreference(
        @ApplicationContext context: Context
    ): AppPreference = DefaultAppPreference(context)
}
