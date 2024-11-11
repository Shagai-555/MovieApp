package com.example.movieapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.Movie
import com.example.movieapp.repository.MovieRepository
import kotlinx.coroutines.launch

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {
    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> get() = _movies

    fun fetchNowPlaying() = viewModelScope.launch {
        val response = repository.getNowPlaying()
        _movies.postValue(response.body()?.results)
    }

    fun searchMovies(query: String) = viewModelScope.launch {
        val response = repository.searchMovies(query)
        _movies.postValue(response.body()?.results)
    }
}

