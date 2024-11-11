package com.example.movieapp.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.data.Movie
import com.example.movieapp.databinding.ItemMovieBinding
import com.squareup.picasso.Picasso

class MovieAdapter(
    private val movieList: List<Movie>,
    private val onClick: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movieList[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int = movieList.size

    inner class MovieViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.movieTitle.text = movie.title

            val imageUrl = "https://image.tmdb.org/t/p/w500${movie.poster_path}"
            Picasso.get().load(imageUrl).into(binding.moviePoster)

            binding.root.setOnClickListener {
                onClick(movie)
            }
        }
    }
}
