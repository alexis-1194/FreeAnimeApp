package com.example.jikanapp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.jikanapp.R
import com.example.jikanapp.databinding.ItemListAnimeBinding
import com.example.jikanapp.domain.model.full.Anime

class ListAdapter(
    private val animeList: List<Anime>,
    private val listener: OnImageClickListener
) :
    RecyclerView.Adapter<ListAdapter.AnimeViewHolder>() {

    // Interfaz para el click listener
    interface OnImageClickListener {
        fun onImageClick(anime: Anime)
    }

    inner class AnimeViewHolder(val binding: ItemListAnimeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(anime: Anime) {
            // Asigna el click listener a la imagen
            binding.imageView.setOnClickListener {
                listener.onImageClick(anime)  // Llama al método cuando se hace click
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val binding =
            ItemListAnimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        val anime = animeList[position]
        holder.binding.titleTextView.text = anime.title
        holder.binding.imageView.load(anime.images.jpg.image_url) // Carga la imagen usando Coil

        // Aplicar animación de entrada usando ViewBinding
        val animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.slide_in)
        holder.binding.root.startAnimation(animation) // Cambiado a binding.root

        // Asignar listener
        holder.bind(anime)
    }


    override fun getItemCount() = animeList.size
}