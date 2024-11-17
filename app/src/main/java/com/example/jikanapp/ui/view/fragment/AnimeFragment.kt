package com.example.jikanapp.ui.view.fragment

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
import com.example.jikanapp.Adapter.SliderAdapter
import com.example.jikanapp.databinding.FragmentAnimeBinding
import com.example.jikanapp.ui.state.UiState
import com.example.jikanapp.ui.viewmodel.AnimeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AnimeFragment : Fragment() {

//        private lateinit var viewModel: AnimeViewModel
    private val viewModel: AnimeViewModel by viewModels()
    private lateinit var adapter: SliderAdapter
    /*Usar un tipo nullable para _binding permite manejar mejor la vida del Fragment.
    Puedes establecer _binding en null en onDestroyView() para
    evitar accesos indebidos a las vistas después de que el Fragment haya sido destruido.*/
    private var _binding: FragmentAnimeBinding? = null
    private val binding get() = _binding!!


    // Variable para efecto de Animación deslizante
    private var isSliding = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnimeBinding.inflate(inflater, container, false)

        // Inicialmente ocultamos el ViewPager hasta que los datos estén listos

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Forma anterior de obtener datos del viewModel
        // Crear instancia de AnimeRepository y GetPopularAnimesUseCase
//        val animeRepository = AnimeRepository() // O inyecta el repositorio
//        val getPopularAnimesUseCase = GetPopularAnimesUseCase(animeRepository)
//        val viewModelFactory = AnimeViewModelFactory(getPopularAnimesUseCase)

//        viewModel = ViewModelProvider(this, viewModelFactory)[AnimeViewModel::class.java]


        // Inicializar el ViewPager2
        binding.viewPager.adapter = null
        viewLifecycleOwner.lifecycleScope.launch { //Aveces no es necesario especificar viewLifecycleOwner, pero no siempre ocurre por ejemplo en ListFragment no se puede omitir
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.animeStateFlowPopular.collect { uiState ->
                    when (uiState) {
                        is UiState.Loading -> {
                            // Mostrar un spinner o algún indicador de carga

                            binding.viewPager.alpha = 0f
                        }

                        is UiState.Success -> {
                            adapter = SliderAdapter(uiState.data)

                            binding.viewPager.animate()
                                .alpha(1f)
                                .setDuration(750) // Duración de la animación (1 segundo)
                                .start()
                            binding.viewPager.adapter = adapter
                            // Animación para que el ViewPager se desvanezca al aparecer

                            // Iniciar el deslizamiento automático
                            startAutoSlider()
                        }

                        is UiState.Error -> {
                            // Mostrar un mensaje de error
                            Toast.makeText(requireContext(), uiState.message, Toast.LENGTH_LONG)
                                .show()
                            binding.viewPager.alpha = 0f

                        }

                        is UiState.Error -> TODO()
                        UiState.Loading -> TODO()
                        is UiState.Success -> TODO()
                    }
                }
            }
        }
    }

    private suspend fun startAutoSlider() {
        isSliding = true
//        lifecycleScope.launch { // Por el momento no es necesario lanzar otra corrutina ya que se puede tomar la corrutina superior que esta invocando a la función
            while (isSliding) {
                val currentItem = binding.viewPager.currentItem
                val nextItem = if (currentItem == adapter.itemCount - 1) 0 else currentItem + 1
                binding.viewPager.setCurrentItem(nextItem, true)
                delay(3000) // Espera 3 segundos antes de cambiar
            }
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        isSliding = false // Detener el deslizamiento automático
    }

    override fun onPause() {
        super.onPause()
        isSliding = false // Detener el deslizamiento automático de forma manual
    }
}