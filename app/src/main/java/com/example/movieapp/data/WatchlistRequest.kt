package com.example.movieapp.data

data class WatchlistRequest(
    val media_type: String = "movie",
    val media_id: Int,
    val watchlist: Boolean = true
)