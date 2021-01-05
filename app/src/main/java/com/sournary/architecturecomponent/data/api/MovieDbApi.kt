package com.sournary.architecturecomponent.data.api

import com.sournary.architecturecomponent.model.Movie
import com.sournary.architecturecomponent.model.VideoListResponse
import com.sournary.architecturecomponent.model.MovieListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Class defines network calls in order to get data from movie-db server.
 */
interface MovieDbApi {

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(@Query("page") page: Int): MovieListResponse

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("page") page: Int): MovieListResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(@Query("page") page: Int): MovieListResponse

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(@Query("page") page: Int): MovieListResponse

    @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Query("page") page: Int,
        @Query("with_genres") genreId: Int
    ): MovieListResponse

    @GET("genre/movie/list")
    suspend fun getGenres(): GenreResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(@Path("movie_id") id: Int): Movie

    @GET("movie/{movie_id}/similar")
    suspend fun getRelatedMovies(
        @Path("movie_id") id: Int,
        @Query("page") page: Int
    ): MovieListResponse

    @GET("movie/{movie_id}/videos")
    suspend fun getVideos(@Path("movie_id") id: Int): VideoListResponse

}
