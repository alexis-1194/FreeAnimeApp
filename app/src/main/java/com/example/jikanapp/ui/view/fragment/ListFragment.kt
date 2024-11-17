package com.example.jikanapp.ui.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.example.jikanapp.Adapter.ListAdapter
import com.example.jikanapp.databinding.FragmentListBinding
import com.example.jikanapp.domain.model.full.Anime
import com.example.jikanapp.ui.state.UiState
import com.example.jikanapp.ui.view.MainActivity
import com.example.jikanapp.ui.view.main.ActivityDetail
import com.example.jikanapp.ui.viewmodel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListFragment : Fragment(), ListAdapter.OnImageClickListener {

    private val viewModel: ListViewModel by viewModels()
    private lateinit var adapter: ListAdapter
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    var recyclerViewPosition: Int = 0 // Para almacenar la posición
    // Definir la constante NO_POSITION si no está disponible
    val NO_POSITION = -1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//
//        // Configura el LayoutManager, se puede hacer desde XML
////        binding.recyclerList.layoutManager = LinearLayoutManager(requireContext())
//
//         Obtener el nombre del anime del bundle y guardarlo en el SavedStateHandle
//        val nombreAnime = arguments?.getString(MainActivity.NOMBRE_ANIME)
//        if (!nombreAnime.isNullOrBlank()) {
//            viewModel.savedStateHandle[MainActivity.NOMBRE_ANIME] = nombreAnime
//        }
//
        // Inicializar SnapHelper, se utiliza para no dejar las imagenes a medias al hacer scroll sobre el RecyclerView
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.recyclerList)


        binding.recyclerList.adapter = null
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.animeStateFlowSearchByName.collect { uiState ->
                    when (uiState) {
                        is UiState.Loading -> {
                            // Mostrar un spinner o algún indicador de carga
                        }

                        is UiState.Success -> {
                            adapter = ListAdapter(uiState.data, this@ListFragment)
                            binding.recyclerList.adapter = adapter
                        }

                        is UiState.Error -> {
                            // Mostrar un mensaje de error
                            Toast.makeText(requireContext(), uiState.message, Toast.LENGTH_LONG)
                                .show()
                        }

                        is UiState.Error -> TODO()
                        UiState.Loading -> TODO()
                        is UiState.Success -> TODO()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onImageClick(anime: Anime) {
        cargarDetalleAnime(anime)
    }

    private fun cargarDetalleAnime(obj: Anime) {
        val intent = Intent(requireContext(), ActivityDetail::class.java)
        intent.putExtra(ANIME_OBJ, obj)
        startActivity(intent)
    }

    companion object {
        val ANIME_OBJ: String = "object_anime"
    }

    override fun onPause() {
        super.onPause()
        recyclerViewPosition = (binding.recyclerList.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
    }

    override fun onResume() {
        super.onResume()
        if (recyclerViewPosition != NO_POSITION) {
            binding.recyclerList.layoutManager?.scrollToPosition(recyclerViewPosition)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("recycler_position", recyclerViewPosition)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            recyclerViewPosition = savedInstanceState.getInt("recycler_position", NO_POSITION)
        }
    }

}