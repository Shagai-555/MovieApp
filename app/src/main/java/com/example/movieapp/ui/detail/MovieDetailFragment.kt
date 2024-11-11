package com.example.movieapp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.movieapp.api.RetrofitInstance
import com.example.movieapp.data.MovieDetails
import com.example.movieapp.databinding.FragmentMovieDetailBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private var movieId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        arguments?.let {
            movieId = it.getInt("movie_id", 0)
        }

        loadMovieDetails()

        return root
    }

    private fun loadMovieDetails() {
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.getMovieDetails(movieId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val movieDetails = response.body()

                        if (movieDetails != null) {
                            binding.movieTitle.text = movieDetails.title
                            binding.movieDescription.text = movieDetails.overview

                            val genres = movieDetails.genres.joinToString(", ") { it.name }
                            binding.movieGenres.text = "Genres: $genres"

                            binding.movieReleaseDate.text = "Release Date: ${movieDetails.release_date}"

                            binding.movieVoteAverage.text = "Vote Average: ${movieDetails.vote_average}"

                            binding.movieRuntime.text = "Runtime: ${movieDetails.runtime} min"

                            val cast = movieDetails.credits.cast.joinToString(", ") { "${it.name} as ${it.character}" }
                            binding.movieCast.text = "Cast: $cast"

                            val imageUrl = "https://image.tmdb.org/t/p/w500${movieDetails.poster_path}"
                            Picasso.get().load(imageUrl).into(binding.moviePoster)
                        }
                    } else {
                        Toast.makeText(context, "Error fetching movie details", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
