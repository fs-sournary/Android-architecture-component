package com.sournary.architecturecomponent.di

import com.sournary.architecturecomponent.repository.MovieRepository
import org.koin.dsl.module

/**
 * The file defines app's repositories.
 */
val repositoryModule = module {
    single { MovieRepository(get()) }
}
