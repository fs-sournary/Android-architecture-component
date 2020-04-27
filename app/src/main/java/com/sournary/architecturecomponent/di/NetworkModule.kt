package com.sournary.architecturecomponent.di

import com.sournary.architecturecomponent.util.NetworkHelper
import org.koin.dsl.module

/**
 * The file defines modules that work with network.
 */
val networkModule = module {
    single { NetworkHelper.createMovieDbApi() }
}
