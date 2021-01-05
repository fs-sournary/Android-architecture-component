package com.sournary.architecturecomponent.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * The model of genre.
 */
@Parcelize
@Entity(tableName = "genre")
data class Genre(
    @SerializedName("id")
    @PrimaryKey
    val id: Int,
    @SerializedName("name")
    val name: String? = null
) : Parcelable {

    companion object {

        val SAVED_GENRES = listOf(
            Genre(1000, "Now playing"),
            Genre(1001, "Popular"),
            Genre(1002, "Top rated"),
            Genre(1003, "Upcoming")
        )
    }
}
