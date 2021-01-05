package com.sournary.architecturecomponent.ext

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.observe
import com.sournary.architecturecomponent.repository.result.Event

fun <T> LiveData<Event<T>>.observeEvent(owner: LifecycleOwner, action: (T) -> Unit) {
    observe(owner) { event ->
        event.getContentIfNotHandled()?.let { action(it) }
    }
}

/**
 * Combines a [LiveData] with another [LiveData] via [combiner] and return a [Result]'s [LiveData].
 * If one of two [LiveData]s triggers, the [Result]'s [LiveData] also might triggers.
 */
fun <A, B, Result> LiveData<A>.combine(
    other: LiveData<B>,
    combiner: (A, B) -> Result
): LiveData<Result> {
    val result = MediatorLiveData<Result>()
    result.addSource(this) { a ->
        val b = other.value ?: return@addSource
        result.postValue(combiner(a, b))
    }
    result.addSource(other) { b ->
        val a = this.value ?: return@addSource
        result.postValue(combiner(a, b))
    }
    return result
}

/**
 * Combines a [LiveData] with other two [LiveData]s via [combiner] and return a [Result]'s [LiveData].
 * If one of three [LiveData]s triggers, the [Result]'s [LiveData] also might triggers.
 */
fun <A, B, C, Result> LiveData<A>.combine(
    other1: LiveData<B>,
    other2: LiveData<C>,
    combiner: (A, B, C) -> Result
): LiveData<Result> {
    val result = MediatorLiveData<Result>()
    result.addSource(this) { a ->
        val b = other1.value ?: return@addSource
        val c = other2.value ?: return@addSource
        result.postValue(combiner(a, b, c))
    }
    result.addSource(other1) { b ->
        val a = this.value ?: return@addSource
        val c = other2.value ?: return@addSource
        result.postValue(combiner(a, b, c))
    }
    result.addSource(other2) { c ->
        val a = this.value ?: return@addSource
        val b = other1.value ?: return@addSource
        result.postValue(combiner(a, b, c))
    }
    return result
}
