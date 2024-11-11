package com.example.movieapp.data

data class MovieDetails(
    val title: String,
    val overview: String,
    val genres: List<Genre>,
    val release_date: String,
    val vote_average: Double,
    val runtime: Int,
    val credits: Credits
)

data class Genre(val name: String)
data class Credits(val cast: List<Cast>)
data class Cast(val name: String, val character: String)
