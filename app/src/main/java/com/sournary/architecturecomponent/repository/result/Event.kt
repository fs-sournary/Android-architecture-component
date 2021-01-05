package com.sournary.architecturecomponent.repository.result

/**
 * The wrapper class for single-event properties such as show toast, navigation...
 */
open class Event<out T>(private val content: T) {

    private var isHandled = false

    /**
     * Get content if it hasn't been used and then prevent it is used again.
     */
    fun getContentIfNotHandled(): T? = if (isHandled) {
        null
    } else {
        isHandled = true
        content
    }

    /**
     * Get content whenever it was used or not.
     */
    fun peekContent(): T = content
}
