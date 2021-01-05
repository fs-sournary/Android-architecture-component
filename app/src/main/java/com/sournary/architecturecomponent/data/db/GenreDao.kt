package com.sournary.architecturecomponent.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.sournary.architecturecomponent.model.Genre
import kotlinx.coroutines.flow.Flow

/**
 * The dao interface defines methods that is used to query movie category in app's data.
 */
@Dao
interface GenreDao : BaseDao<Genre> {

    @Query("SELECT * FROM genre")
    fun getGenres(): LiveData<List<Genre>>

    @Query("SELECT * FROM genre")
    fun getFlowGenres(): Flow<List<Genre>>
}
