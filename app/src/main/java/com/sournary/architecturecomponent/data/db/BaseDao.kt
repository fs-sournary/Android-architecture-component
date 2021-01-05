package com.sournary.architecturecomponent.data.db

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

/**
 * The base Dao class defines all common methods to query database.
 */
interface BaseDao<T> {

    /**
     * Insert T object in the database.
     */
    @Insert
    suspend fun insert(obj: T)

    /**
     * Insert a list of T in the database.
     */
    @Insert
    suspend fun insert(obj: List<T>)

    /**
     * Update T in the database.
     */
    @Update
    suspend fun update(obj: T)

    /**
     * Delete T in the database.
     */
    @Delete
    suspend fun delete(obj: T)

}
