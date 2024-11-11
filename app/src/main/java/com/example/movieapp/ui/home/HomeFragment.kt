package com.example.movieapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.api.RetrofitInstance
import com.example.movieapp.data.Movie
import com.example.movieapp.data.MovieResponse
import com.example.movieapp.databinding.FragmentHomeBinding
import com.example.movieapp.ui.home.adapters.MovieAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerViews()
        loadMovies()

        return root
    }

    private fun setupRecyclerViews() {
        // Initialize the layout managers
        binding.nowPlayingRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.upcomingRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.topRatedRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.popularRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun loadMovies() {
        // Load movies for each category
        fetchMovies("now_playing", binding.nowPlayingRecyclerView)
        fetchMovies("upcoming", binding.upcomingRecyclerView)
        fetchMovies("top_rated", binding.topRatedRecyclerView)
        fetchMovies("popular", binding.popularRecyclerView)
    }

    private fun fetchMovies(category: String, recyclerView: RecyclerView) {
        // Use lifecycleScope for better coroutine management
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Get the response from the API
                val response: Response<MovieResponse> = RetrofitInstance.api.getMoviesByCategory(category)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val movies: List<Movie> = response.body()?.results ?: emptyList()
                        movieAdapter = MovieAdapter(movies) { movie -> onMovieClicked(movie) }
                        recyclerView.adapter = movieAdapter
                    } else {
                        Toast.makeText(requireContext(), "Failed to load movies", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun onMovieClicked(movie: Movie) {
        val bundle = Bundle().apply {
            putInt("movie_id", movie.id)
        }
        findNavController().navigate(R.id.action_homeFragment_to_movieDetailFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
