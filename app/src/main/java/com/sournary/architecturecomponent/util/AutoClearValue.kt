package com.sournary.architecturecomponent.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Wrapper class creates a value that is clean up when its fragment is destroyed.
 * Use case: Binding, Adapter...
 */
class AutoClearValue<T : Any>(fragment: Fragment) : ReadWriteProperty<Fragment, T> {

    private var _value: T? = null

    init {
        fragment.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                _value = null
            }
        })
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T =
        _value ?: throw IllegalStateException("The AutoClearValue might not be initialized")

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        _value = value
    }

}
