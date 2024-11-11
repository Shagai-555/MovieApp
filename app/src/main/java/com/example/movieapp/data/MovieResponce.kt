package com.example.movieapp.data

data class MovieResponse(
    val results: List<Movie>
)

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val release_date: String,
    val vote_average: Double,
    val poster_path: String
)

