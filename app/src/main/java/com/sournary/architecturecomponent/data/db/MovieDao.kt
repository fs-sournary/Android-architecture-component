package com.sournary.architecturecomponent.data.db

import androidx.room.Dao
import com.sournary.architecturecomponent.model.Movie

/**
 * The dao interface defines methods that is used to query to movie in the app's database.
 */
@Dao
interface MovieDao: BaseDao<Movie>
