package com.sournary.architecturecomponent.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sournary.architecturecomponent.repository.result.Event

/**
 * The shared ViewModel provides the navigation that is provided by menu of navigation.
 */
class MainViewModel : ViewModel() {

    private val _lockNavigation = MutableLiveData<Boolean>()
    val lockNavigation: LiveData<Boolean> = _lockNavigation

    private val _openNavigation = MutableLiveData<Event<Unit>>()
    val openNavigation: LiveData<Event<Unit>> = _openNavigation

    fun setLockNavigation(lock: Boolean) {
        _lockNavigation.value = lock
    }

    fun openNavigation() {
        _openNavigation.value = Event(Unit)
    }
}
