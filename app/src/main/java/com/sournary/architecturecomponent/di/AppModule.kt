package com.sournary.architecturecomponent.di

import com.sournary.architecturecomponent.db.MovieDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

/**
 * The files defines modules that work with local app such as database, preference...
 */
val appModule = module {
    single { MovieDatabase.getInstance(androidApplication()).getMovieDao() }
    single { MovieDatabase.getInstance(androidApplication()).getGenre() }
}
