package com.example.movieapp.api

import com.example.movieapp.data.Credits
import com.example.movieapp.data.MovieDetails
import com.example.movieapp.data.MovieResponse
import com.example.movieapp.data.WatchlistRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDbApi {
    @GET("movie/now_playing")
    suspend fun getNowPlaying(@Query("page") page: Int = 1): Response<MovieResponse>

    @GET("movie/top_rated")
    suspend fun getTopRated(@Query("page") page: Int = 1): Response<MovieResponse>

    @GET("movie/popular")
    suspend fun getPopular(@Query("page") page: Int = 1): Response<MovieResponse>

    @GET("search/movie")
    suspend fun searchMovies(@Query("query") query: String, @Query("page") page: Int = 1): Response<MovieResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: Int): Response<MovieDetails>

    @GET("movie/{movie_id}/credits")
    suspend fun getCredit(@Path("movie_id") movieId: Int): Response<Credits>

    @GET("movie/{category}")
    suspend fun getMoviesByCategory(
        @Path("category") category: String,
        @Query("page") page: Int = 1
    ): Response<MovieResponse>

    @POST("account/{account_id}/watchlist")
    suspend fun addMovieToWatchlist(
        @Path("account_id") accountId: Int = 21592939,
        @Body movie: WatchlistRequest
    ): Response<MovieResponse>

    @GET("account/{account_id}/watchlist/movies")
    suspend fun getWatchList(
        @Path("account_id") accountId: Int = 21592939
    ): Response<MovieResponse>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(@Query("page") page: Int = 1): Response<MovieResponse>
}
