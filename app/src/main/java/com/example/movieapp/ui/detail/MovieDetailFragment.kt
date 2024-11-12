package com.example.movieapp.ui.detail

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.CastAdapter
import com.example.movieapp.R
import com.example.movieapp.api.RetrofitInstance
import com.example.movieapp.data.WatchlistRequest
import com.example.movieapp.databinding.FragmentMovieDetailBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private var movieId: Int = 0
    private lateinit var watchlistButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        watchlistButton = binding.root.findViewById(R.id.watchlistButton)

        watchlistButton.setOnClickListener {
            addToWatchlist()
        }

        val backButton: ImageButton = binding.root.findViewById(R.id.backButton)
        backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        arguments?.let {
            movieId = it.getInt("movie_id", 0)
        }

        binding.loadingIndicator.visibility = View.VISIBLE
        setupCastRecyclerView()
        loadMovieDetails()

        return root
    }

    private fun setupCastRecyclerView() {
        binding.castRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.castRecyclerView.adapter = CastAdapter(emptyList())
    }

    private fun loadMovieDetails() {
        lifecycleScope.launch {
            try {

                binding.loadingIndicator.visibility = View.VISIBLE
                val response = RetrofitInstance.api.getMovieDetails(movieId)
                val creditRes = RetrofitInstance.api.getCredit(movieId)

                withContext(Dispatchers.Main) {
                    binding.loadingIndicator.visibility = View.GONE

                    if (response.isSuccessful && creditRes.isSuccessful) {
                        val movieDetails = response.body()
                        val credits = creditRes.body()

                        if (movieDetails != null) {
                            binding.movieTitle.text = movieDetails.title
                            binding.movieDescription.text = movieDetails.overview

                            val genres = movieDetails.genres.joinToString(" | ") { it.name }
                            binding.movieGenres.text = genres

                            binding.movieReleaseDate.text = Html.fromHtml(
                                "Release Date: <b><i>${movieDetails.release_date}</i></b>",
                                Html.FROM_HTML_MODE_LEGACY
                            )

                            binding.movieVoteAverage.text = Html.fromHtml(
                                "Vote Average: <b><i>${movieDetails.vote_average}⭐</i></b>",
                                Html.FROM_HTML_MODE_LEGACY
                            )

                            binding.movieRuntime.text = Html.fromHtml(
                                "Runtime: <b><i>${movieDetails.runtime} minutes</i></b>",
                                Html.FROM_HTML_MODE_LEGACY
                            )

                            val productionCompanies = movieDetails.production_companies.joinToString(" | ") { it.name }
                            binding.movieProductionCompanies.text = productionCompanies

                            binding.movieRevenue.text = Html.fromHtml(
                                "Revenue: <b><i>$${movieDetails.revenue}</i></b>",
                                Html.FROM_HTML_MODE_LEGACY
                            )

                            val backdropUrl = "https://image.tmdb.org/t/p/w500${movieDetails.backdrop_path}"
                            Picasso.get().load(backdropUrl).into(binding.movieBackdrop)

                            val imageUrl = "https://image.tmdb.org/t/p/w500${movieDetails.poster_path}"
                            Picasso.get().load(imageUrl).into(binding.moviePoster)

                            if (credits != null) {
                                (binding.castRecyclerView.adapter as CastAdapter).updateCast(credits.cast)
                            }
                        } else {
                            Toast.makeText(context, "Movie details not available", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Failed to load movie details", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                binding.loadingIndicator.visibility = View.GONE
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addToWatchlist() {
        lifecycleScope.launch {
            try {
                binding.loadingIndicator.visibility = View.VISIBLE
                val response = RetrofitInstance.api.addMovieToWatchlist(
                    movie = WatchlistRequest(media_id = movieId)
                )
                binding.loadingIndicator.visibility = View.GONE

                if (response.isSuccessful) {
                    watchlistButton.setImageResource(R.drawable.ic_watchlist_filled)
                    Toast.makeText(context, "Added to Watchlist!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to add to Watchlist", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

