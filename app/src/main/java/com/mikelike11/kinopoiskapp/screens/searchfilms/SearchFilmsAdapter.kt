package com.mikelike11.kinopoiskapp.screens.searchfilms

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mikelike11.kinopoiskapp.data.model.Film
import com.mikelike11.kinopoiskapp.databinding.FilmItemListBinding


class SearchFilmsAdapter(
    private val onItemClick: (Film) -> Unit,
    private val onItemLongClick: (Film, Int) -> Unit
) : RecyclerView.Adapter<MainViewHolder>() {
    var films = mutableListOf<Film>()
    fun setFilmList(films: List<Film>) {
        this.films = films.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FilmItemListBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val film = films[position]
        holder.binding.itemFilmTitle.text = film.nameRu
        holder.binding.itemFilmCategory.text = "${film.genres.get(0).genre} ( ${film.year} )"
        holder.binding.itemFilmCard.setOnClickListener {
            onItemClick(film)
        }
        holder.binding.itemFilmCard.setOnLongClickListener {
            film.isInFavorites = film.isInFavorites.not()
            onItemLongClick(film, position)
            true
        }
        holder.binding.itemFilmIsFavorites.visibility =
            if (film.isInFavorites) View.VISIBLE else View.INVISIBLE
        Glide.with(holder.itemView.context).load(film.posterUrlPreview).apply(
            RequestOptions.bitmapTransform(
                RoundedCorners(15)
            )
        ).into(holder.binding.itemFilmPoster)
    }

    override fun getItemCount(): Int {
        return films.size
    }
}

class MainViewHolder(val binding: FilmItemListBinding) : RecyclerView.ViewHolder(binding.root) {
}