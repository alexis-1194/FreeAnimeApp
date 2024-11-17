package com.example.jikanapp.Adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.jikanapp.domain.model.full.Anime
import com.example.jikanapp.ui.view.fragment.GeneralInfoFragment
import com.example.jikanapp.ui.view.fragment.SynopsisFragment
import com.example.jikanapp.ui.view.fragment.VideoFragment


class PagerAdapter(
    fragment: Fragment,
    private val anime: Anime
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> GeneralInfoFragment.newInstance(anime)  // Primer fragmento, por ejemplo, información general
            1 -> SynopsisFragment.newInstance(anime)     // Segundo fragmento, para la sinopsis
            2 -> VideoFragment.newInstance(anime)        // Tercer fragmento, para el video
            else -> throw IllegalStateException("Posición inválida")
        }
    }
}