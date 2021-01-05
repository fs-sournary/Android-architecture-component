package com.sournary.architecturecomponent.di

import com.sournary.architecturecomponent.data.api.MovieDbApi
import com.sournary.architecturecomponent.data.db.GenreDao
import com.sournary.architecturecomponent.repository.home.DefaultHomeRepository
import com.sournary.architecturecomponent.repository.home.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

/**
 * The file defines app's repositories.
 */
@InstallIn(ApplicationComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideHomeRepository(
        movieDbApi: MovieDbApi,
        genreDao: GenreDao
    ): HomeRepository = DefaultHomeRepository(movieDbApi, genreDao)
}
