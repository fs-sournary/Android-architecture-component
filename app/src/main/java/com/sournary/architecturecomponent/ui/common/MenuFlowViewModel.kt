package com.sournary.architecturecomponent.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * The shared ViewModel provides the navigation that is provided by menu of navigation.
 */
class MenuFlowViewModel : ViewModel() {

    private val _lockNavigation = MutableLiveData<Boolean>()
    val lockNavigation: LiveData<Boolean>
        get() = _lockNavigation

    private val _openNavigation = MutableLiveData<Any>()
    val openNavigation: LiveData<Any>
        get() = _openNavigation

    fun setLockNavigation(lock: Boolean) {
        _lockNavigation.value = lock
    }

    fun openNavigation() {
        _openNavigation.value = Any()
    }

}
