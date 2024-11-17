package com.example.jikanapp.ui.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.jikanapp.databinding.FragmentSynopsisBinding
import com.example.jikanapp.domain.model.full.Anime
import parcelable

class SynopsisFragment(
//    private val anime: Anime
) : Fragment() {

    private lateinit var binding: FragmentSynopsisBinding
    private lateinit var anime: Anime // Variable de clase para almacenar el objeto Anime

    companion object {
        fun newInstance(anime: Anime): SynopsisFragment {
            val fragment = SynopsisFragment()
            val bundle = Bundle()
            bundle.putParcelable(ListFragment.ANIME_OBJ, anime) // Usar putSerializable o putParcelable
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout del fragmento y configurar los datos que necesites
        binding = FragmentSynopsisBinding.inflate(layoutInflater)
        anime = arguments?.parcelable<Anime>(ListFragment.ANIME_OBJ)!!
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sinopsisText.text = anime.synopsis
        binding.startDateText.text = anime.aired.from
        binding.endDateText.text = anime.aired.to
        binding.typeText.text = anime.type
    }
}