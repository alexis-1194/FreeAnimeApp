package com.example.jikanapp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jikanapp.databinding.ItemLinkBinding
import com.example.jikanapp.domain.model.full.Anime
import com.example.jikanapp.domain.model.full.scraping.EpisodeLink

class ListLinkAdapter(
    private val episodeLinkList: List<EpisodeLink>,
    private val listener: OnItemLinkClickListener,
) :
    RecyclerView.Adapter<ListLinkAdapter.AnimeViewHolder>() {

    // Interfaz para el click listener
    interface OnItemLinkClickListener {
        fun onItemListLinkClick(episodeLink: EpisodeLink)
    }

    inner class AnimeViewHolder(val binding: ItemLinkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(episodeLink: EpisodeLink) {
            // Asigna el click listener a la imagen
            binding.titleText.setOnClickListener {
                listener.onItemListLinkClick(episodeLink)  // Llama al método cuando se hace click
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val binding = ItemLinkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        val episodeLink = episodeLinkList[position]
        holder.binding.titleText.text = episodeLink.link

        // Asignar listener
        holder.bind(episodeLink)
    }

    override fun getItemCount() = episodeLinkList.size
}