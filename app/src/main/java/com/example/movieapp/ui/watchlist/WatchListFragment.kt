package com.example.movieapp.ui.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.R
import com.example.movieapp.api.RetrofitInstance
import com.example.movieapp.data.Movie
import com.example.movieapp.databinding.FragmentWatchlistBinding
import com.example.movieapp.ui.home.adapters.MovieAdapter
import kotlinx.coroutines.launch

class WatchListFragment : Fragment() {

    private var _binding: FragmentWatchlistBinding? = null
    private val binding get() = _binding!!

    private lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWatchlistBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        movieAdapter = MovieAdapter(emptyList()) { movie ->
            onMovieClicked(movie)
        }
        binding.recyclerView.adapter = movieAdapter

        fetchWatchlist()

        return root
    }

    private fun fetchWatchlist() {
        lifecycleScope.launch {
            try {
                binding.loadingIndicator.visibility = View.VISIBLE
                val response = RetrofitInstance.api.getWatchList()

                binding.loadingIndicator.visibility = View.GONE

                if (response.isSuccessful) {
                    val movieResponse = response.body()
                    movieResponse?.let {
                        movieAdapter = MovieAdapter(it.results) { movie ->
                            onMovieClicked(movie)
                        }
                        binding.recyclerView.adapter = movieAdapter
                    }
                } else {
                    Toast.makeText(context, "Failed to load watchlist", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onMovieClicked(movie: Movie) {
        val bundle = Bundle().apply {
            putInt("movie_id", movie.id)
        }
        findNavController().navigate(R.id.action_watchList_to_movieDetailFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
