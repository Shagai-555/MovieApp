package com.example.movieapp.repository
import com.example.movieapp.api.RetrofitInstance

class MovieRepository {
    private val api = RetrofitInstance.api

    suspend fun getNowPlaying() = api.getNowPlaying()
    suspend fun getTopRated() = api.getTopRated()
    suspend fun getPopular() = api.getPopular()
    suspend fun searchMovies(query: String) = api.searchMovies(query)
    suspend fun getMovieDetails(movieId: Int) = api.getMovieDetails(movieId)
}
