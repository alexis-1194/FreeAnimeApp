package com.example.jikanapp.ui.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.jikanapp.Adapter.PagerAdapter
import com.example.jikanapp.databinding.FragmentDetailBinding
import com.example.jikanapp.domain.model.full.Anime
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import parcelable

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Configurar el ViewPager con su adaptador
        val obj = arguments?.parcelable<Anime>(ListFragment.ANIME_OBJ)

        val adapter = obj?.let { PagerAdapter(this, it) }
        binding.viewPager.adapter = adapter

        // Obtener el objeto anime del bundle


        // Vincular TabLayout con ViewPager2
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "General"
                1 -> "Sinopsis"
                2 -> "Video"
                else -> null
            }
        }.attach()

    }
}