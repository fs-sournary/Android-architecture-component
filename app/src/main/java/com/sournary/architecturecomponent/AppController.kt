package com.sournary.architecturecomponent

import android.app.Application
import com.sournary.architecturecomponent.di.appModule
import com.sournary.architecturecomponent.di.networkModule
import com.sournary.architecturecomponent.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

/**
 * The class represents application class of our app
 */
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
