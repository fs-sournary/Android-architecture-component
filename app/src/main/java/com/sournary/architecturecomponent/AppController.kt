package com.sournary.architecturecomponent

import android.app.Application
import com.sournary.architecturecomponent.di.appModule
import com.sournary.architecturecomponent.di.networkModule
import com.sournary.architecturecomponent.di.repositoryModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

/**
 * The class represents application class of our app
 */
@FlowPreview
@ExperimentalCoroutinesApi
class AppController : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        startKoin {
            androidContext(this@AppController)
            modules(listOf(appModule, networkModule, repositoryModule))
        }
    }

}
