package com.example.jikanapp.ui.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import com.example.jikanapp.databinding.FragmentGeneralInfoBinding
import com.example.jikanapp.domain.model.full.Anime
import com.example.jikanapp.util.Utils
import parcelable


class GeneralInfoFragment(
//    private val anime: Anime,
) : Fragment() {

    private lateinit var binding: FragmentGeneralInfoBinding // A diferencia del enfoque donde se utiliza una variable nullable, no necesitas establecer binding a null en onDestroyView() al usar lateinit.
    private lateinit var anime: Anime // Variable de clase para almacenar el objeto Anime

    companion object {
        fun newInstance(anime: Anime): GeneralInfoFragment {
            val fragment = GeneralInfoFragment()
            val bundle = Bundle()
            bundle.putParcelable(ListFragment.ANIME_OBJ, anime) // Usar putSerializable o putParcelable
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflar el layout del fragmento y configurar los datos que necesites
        binding = FragmentGeneralInfoBinding.inflate(layoutInflater)
        anime = arguments?.parcelable<Anime>(ListFragment.ANIME_OBJ)!!
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.animeImage.load(anime.images.jpg.image_url)
        binding.titleText.text = anime.title
        binding.englishTitleText.text = anime.title_english.toString()
        binding.episodiosText.text = anime.episodes.toString()
        binding.scoreText.text = anime.score.toString()
        binding.ratedText.text = anime.rating
        binding.genresText.text = Utils.listToStrings(anime.genres)
        binding.statusText.text = anime.status
        binding.airedText.text = anime.aired.string
        binding.studiosText.text = Utils.listToStrings(anime.studios)


    }

    override fun onPause() {
        super.onPause()
        println("Estamos en el onPause")
    }

    override fun onStop() {
        super.onStop()
        println("Estamos en el onStop")
    }


}