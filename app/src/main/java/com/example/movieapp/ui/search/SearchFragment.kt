package com.example.movieapp.ui.search

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.R
import com.example.movieapp.api.RetrofitInstance
import com.example.movieapp.data.Movie
import com.example.movieapp.databinding.FragmentSearchBinding
import com.example.movieapp.ui.home.adapters.MovieAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.searchRecyclerView.layoutManager = LinearLayoutManager(context)

        binding.searchEditText.addTextChangedListener { text ->
            if (!TextUtils.isEmpty(text)) {
                searchMovies(text.toString())
            }
        }

        return root
    }

    private fun searchMovies(query: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.searchMovies(query)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val movies = response.body()?.results ?: emptyList()
                        movieAdapter = MovieAdapter(movies) { movie -> onMovieClicked(movie) }
                        binding.searchRecyclerView.adapter = movieAdapter
                    } else {
                        Toast.makeText(context, "Error searching movies", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun onMovieClicked(movie: Movie) {
        val bundle = Bundle().apply {
            putInt("movie_id", movie.id)
        }
        findNavController().navigate(R.id.action_searchFragment_to_movieDetailFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
