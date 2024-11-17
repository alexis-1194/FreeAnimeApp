package com.example.jikanapp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.jikanapp.databinding.ItemAnimeBinding
import com.example.jikanapp.domain.model.full.Anime

class SliderAdapter(private val animeList: List<Anime>) :
    RecyclerView.Adapter<SliderAdapter.AnimeViewHolder>() {

    inner class AnimeViewHolder(val binding: ItemAnimeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val binding = ItemAnimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        val anime = animeList[position]
        holder.binding.imageView.load(anime.images!!.jpg!!.image_url) // Carga la imagen usando Coil
    }

    override fun getItemCount() = animeList.size
}