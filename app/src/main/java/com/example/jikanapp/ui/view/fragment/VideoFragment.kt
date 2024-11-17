package com.example.jikanapp.ui.view.fragment

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.jikanapp.ActivityVideoLink
import com.example.jikanapp.Adapter.ListLinkAdapter
import com.example.jikanapp.databinding.FragmentVideoBinding
import com.example.jikanapp.domain.model.full.Anime
import com.example.jikanapp.domain.model.full.scraping.EpisodeLink
import com.example.jikanapp.ui.view.main.ActivityDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import parcelable
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class VideoFragment(
) : Fragment(), ListLinkAdapter.OnItemLinkClickListener {

    private var _binding: FragmentVideoBinding? = null
    private val client = OkHttpClient()
    private val binding get() = _binding!!

    private lateinit var adapter: ListLinkAdapter

    private lateinit var anime: Anime // Variable de clase para almacenar el objeto Anime

    private val episodeLinkList: MutableList<EpisodeLink> = mutableListOf()
    private val downloadLinkList: MutableList<EpisodeLink> = mutableListOf()


    companion object {

        val VIDEO_LINK: String = "video_link"


        fun newInstance(anime: Anime): VideoFragment {
            val fragment = VideoFragment()
            val bundle = Bundle()
            bundle.putParcelable(
                ListFragment.ANIME_OBJ, anime
            ) // Usar putSerializable o putParcelable
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflar el layout del fragmento y configurar los datos que necesites
        _binding = FragmentVideoBinding.inflate(layoutInflater)
        anime = arguments?.parcelable<Anime>(ListFragment.ANIME_OBJ)!!
        return binding.root
    }

    var firstQuery: String = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = null
        firstQuery = anime.title.toString().lowercase()
        fetchData(firstQuery)
    }

//    fun startVideo(){
//        // Get the YouTubePlayerView from the binding
//        val youTubePlayerView: YouTubePlayerView = binding.youtubePlayerView
//
//        // Add lifecycle observer (important to manage the lifecycle of the player)
//        lifecycle.addObserver(youTubePlayerView)
//
//        // Set up the YouTube player
//        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
//            override fun onReady(youTubePlayer: YouTubePlayer) {
//                // Play a YouTube video
//                val videoId =
//                    anime.trailer.youtube_id  // Replace with the actual video ID from YouTube
//                if (videoId != null) {
//                    youTubePlayer.loadVideo(videoId, 0f)
//                }  // Start at the beginning of the video
//            }
//        })
//    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    var splitList: List<String> = mutableListOf()
    private fun fetchData(query: String) {
        var episodeList: MutableList<EpisodeLink> = mutableListOf()
        CoroutineScope(Dispatchers.Main).launch {
            // Usar una corutina para hacer la llamada de red en un hilo en segundo plano
            val result = withContext(Dispatchers.IO) { /* Result tambien puede almacenar withContext para que tome la ultima
            instrucción despues de este bloque de código y la retorne y almacene en result (val result = withContext(Dispatchers.IO))*/
                try {
                    Jsoup.connect("https://www.tokyoinsider.com/anime/search?k=$query").get()
                } catch (e: Exception) {
                    e.printStackTrace()
                    null // Manejo de errores
                }
            }
            // Manejo del resultado, asegúrate de que no sea null
            result?.let {
                // Actualiza la UI o maneja el resultado aquí
                println(it.title()) // Ejemplo de uso
                // Seleccionar los enlaces de los animes
                val animeLinks = it.select("a[href]")
                for (link in animeLinks) {
                    val fullUrl = link.absUrl("href") // Obtiene la URL completa


                    if (fullUrl.matches(Regex("https://www\\.tokyoinsider\\.com/anime/[A-Z]/.*"))) {
                        println("Anime encontrado: $fullUrl")
                        if (fullUrl.substring(37).lowercase() == query
                            ||
                            //fullUrl.substring(37).lowercase() == query + "_(tv)"
                            fullUrl.substring(37).lowercase().contains(query)
                        ) {
                            println("Anime encontrado: $fullUrl")
                            episodeList = fetchEpisodes(fullUrl)
                            withContext(Dispatchers.Main) {
                                episodeList.let {
                                    adapter = ListLinkAdapter(it, this@VideoFragment)
                                    binding.recyclerView.adapter = adapter
                                }
                            }
                            break
                        }
                    }
                }
            }
            // Ejemplo Query = "naruto the last movie"
            //Busqueda recursiva

            fetchUntilValid(episodeList, query)

        } ?: println("La respuesta fue nula.")


    }

    private fun fetchUntilValid(episodeList : MutableList<EpisodeLink>, query: String) {
        splitList = query.split(" ")
        if (episodeList.isEmpty()) {
            //TODO: Se necesita realizar
            // una conexión hacia una URL generica ya que si no se puede obtener algo de la primera solicitud
            // entonces debemos tomar un valor especifico de la primera URL y volver hacer la solicitud
            // podria ser que se repita tomando cada valor de la primera cadena dividida por espacios
            // hasta encontrar un valor finalmente.
            fetchData(splitList[0])
            splitList = splitList.subList(1, splitList.size)
        }
    }

    // Función para obtener los episodios(descargables) de un anime específico
    private suspend fun fetchEpisodes(url: String): MutableList<EpisodeLink> {
        return withContext(Dispatchers.IO) {
            val episodeLinks = mutableListOf<EpisodeLink>()
            try {
                val animePage = Jsoup.connect(url).get()
                val episodes =
                    animePage.select("a[href].download-link") // Seleccionas los enlaces de episodios o la información específica

                for (episode in episodes) {
                    val episodeUrl = episode.absUrl("href")
                    // Selecciona el elemento <a> con la clase "download-link"
                    val episodeTitle = episode.text()
                    println("Episodio encontrado: $episodeUrl Titulo : $episodeTitle")
                    episodeLinks.add(
                        EpisodeLink(
                            title = episodeTitle, link = episodeUrl, releaseDate = ""
                        )
                    )
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
            episodeLinks // Retorna la lista completa cuando termina
        }
    }

    private fun cargarDatosLink(episodeLink: EpisodeLink) {
        val intent = Intent(requireContext(), ActivityVideoLink::class.java)
        intent.putExtra(VIDEO_LINK, episodeLink)
        startActivity(intent)
    }

    fun obtenerArchivosMp4(context: Context): List<File> {
        // Ruta a la carpeta de descargas pública
        val rutaDescargas =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        // Lista para almacenar archivos .mp4
        val archivosMp4 = mutableListOf<File>()

        if (rutaDescargas.exists() && rutaDescargas.isDirectory) {
            val archivos = rutaDescargas.listFiles()
            archivos?.forEach { archivo ->
                if (archivo.isFile && archivo.extension == "mp4") {
                    archivosMp4.add(archivo)
                    Log.d("ArchivosMp4", "Archivo MP4 encontrado: ${archivo.name}")
                }
            }
        } else {
            Log.d("ArchivosMp4", "La carpeta de descargas no existe o no es un directorio")
        }

        return archivosMp4
    }

    fun downloadVideo(url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = Request.Builder().url(url)
                    .header("User-Agent", "Mozilla/5.0")  // Añadir User-Agent
                    .build()

                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) throw Exception("Error en la solicitud: $response")

                    val inputStream: InputStream? = response.body()?.byteStream()
                    val file = File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        "video.mp4"
                    )

                    inputStream?.let { input ->
                        FileOutputStream(file).use { output ->
                            val buffer = ByteArray(1024)
                            var bytesRead: Int
                            while (input.read(buffer).also { bytesRead = it } != -1) {
                                output.write(buffer, 0, bytesRead)
                            }
                            Log.d(
                                "Download",
                                "Video descargado correctamente en: ${file.absolutePath}"
                            )
                        }

                    }
                }
            } catch (e: Exception) {
                Log.e("Download", "Error al descargar el video: ${e.message}")
            }
        }
    }

    fun downloadFile(url: String, fileName: String) {
        // Obtener el contexto del sistema de servicios
        val request = DownloadManager.Request(Uri.parse(url)).apply {
            // Configurar la información de la descarga
            setTitle("Descargando $fileName")
            setDescription("Descargando el archivo...")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS, fileName
            ) // Guardar en la carpeta de descargas
        }

        // Obtener el DownloadManager y iniciar la descarga
        val downloadManager =
            requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }

    override fun onItemListLinkClick(episodeLink: EpisodeLink) {
        cargarDatosLink(episodeLink)
    }

    private fun cargar(obj: Anime) {
        val intent = Intent(requireContext(), ActivityDetail::class.java)
        intent.putExtra(ListFragment.ANIME_OBJ, obj)
        startActivity(intent)
    }


}