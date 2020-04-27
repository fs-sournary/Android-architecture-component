package com.sournary.architecturecomponent.api

import com.sournary.architecturecomponent.data.MovieListResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Class defines network calls in order to get data from movie-db server.
 */
interface MovieDbApi {

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(@Query("page") page: Int): MovieListResponse

}
