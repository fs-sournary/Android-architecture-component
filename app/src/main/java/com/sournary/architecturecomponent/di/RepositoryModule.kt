package com.sournary.architecturecomponent.di

import com.sournary.architecturecomponent.repository.MovieRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.dsl.module

/**
 * The file defines app's repositories.
 */
@FlowPreview
@ExperimentalCoroutinesApi
val repositoryModule = module {
    single { MovieRepository(get(), get()) }
}
