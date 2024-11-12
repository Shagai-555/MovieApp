package com.example.movieapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.data.Cast
import com.squareup.picasso.Picasso

class CastAdapter(private var castList: List<Cast>) : RecyclerView.Adapter<CastAdapter.CastViewHolder>() {

    inner class CastViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val profileImage: ImageView = view.findViewById(R.id.castProfileImage)
        val name: TextView = view.findViewById(R.id.castName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cast, parent, false)
        return CastViewHolder(view)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val cast = castList[position]
        val profileUrl = "https://image.tmdb.org/t/p/w500${cast.profile_path}"

        Picasso.get().load(profileUrl).placeholder(R.drawable.placeholder).into(holder.profileImage)

        holder.name.text = cast.name

        holder.profileImage.setOnClickListener {
            Toast.makeText(
                holder.itemView.context,
                "Character: ${cast.character}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun getItemCount(): Int = castList.size

    fun updateCast(newCastList: List<Cast>) {
        castList = newCastList
        notifyDataSetChanged()
    }
}
