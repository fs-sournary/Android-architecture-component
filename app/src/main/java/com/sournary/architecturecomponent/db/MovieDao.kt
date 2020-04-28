package com.sournary.architecturecomponent.db

import androidx.room.Dao
import com.sournary.architecturecomponent.data.Movie

/**
 * The dao interface defines methods that is used to query to movie in the app's database.
 */
@Dao
interface MovieDao: BaseDao<Movie>
