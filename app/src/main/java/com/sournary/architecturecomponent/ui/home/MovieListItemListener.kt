package com.sournary.architecturecomponent.ui.home

import com.sournary.architecturecomponent.data.Movie

/**
 * The interface define a method that is invoked when user clicks item's layout.
 */
interface MovieListItemListener {

    fun onItemClick(movie: Movie)

}
