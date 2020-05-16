package com.sournary.architecturecomponent.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * The custom [MutableLiveData] class that is used for single events like navigation, show toast...
 * Observer only trigger when we call `setValue` or `call()` and does not trigger again when
 * configuration change occurs.
 */
class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val pending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, Observer {
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(it)
            }
        })
    }

    override fun setValue(value: T?) {
        pending.set(true)
        super.setValue(value)
    }

    /**
     * Method triggers an event with no data.
     */
    fun call() {
        value = null
    }

}
