package com.example.jikanapp

import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.jikanapp.databinding.ActivityVideoLinkBinding
import com.example.jikanapp.domain.model.full.scraping.EpisodeLink
import com.example.jikanapp.ui.view.fragment.VideoFragment
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

class ActivityVideoLink : AppCompatActivity() {

    private lateinit var binding: ActivityVideoLinkBinding

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoLinkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val videoLink = intent.parcelable<EpisodeLink>(VideoFragment.VIDEO_LINK)
        binding.progressBar.visibility = View.GONE

        CoroutineScope(Dispatchers.Main).launch {
            videoLink?.let { fetchEpisodes(it) }
        }

    }

    private suspend fun fetchEpisodes(obj: EpisodeLink): MutableList<EpisodeLink> {
        return withContext(Dispatchers.IO) {
            val episodeLinks = mutableListOf<EpisodeLink>()
            try {
                val animePage = Jsoup.connect(obj.link).get()
                val episodes =
                    animePage.select("a[href]") // Seleccionas los enlaces de episodios o la información específica

                for (episode in episodes) {
                    val episodeUrl = episode.absUrl("href")
                    // Filtra solo los enlaces que terminan en .mp4 o .mkv
                    if (episodeUrl.endsWith(".mp4") || episodeUrl.endsWith(".mkv")) {
                        val episodeTitle = episode.text()
                        println("Episodio encontrado: $episodeUrl Titulo : $episodeTitle")
//                        episodeLinks.add(
//                            EpisodeLink(
//                                title = obj.title,
//                                link = episodeUrl,
//                                releaseDate = "" // Puedes actualizar el releaseDate si tienes esa información disponible
//                            )
//                        )
                        val episode = EpisodeLink(
                            title = obj.title,
                            link = episodeUrl,
                            releaseDate = "" // Puedes actualizar el releaseDate si tienes esa información disponible
                        )
                        withContext(Dispatchers.Main) {
                            cargarVideoLinks(episode)
                            episodeLinks.add(episode)
                        }
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
            episodeLinks // Retorna la lista completa cuando termina
        }
    }


    private fun cargarVideoLinks(episodeLink: EpisodeLink) {

        episodeLink.let { element ->

            // Crear CardView
            val cardView = this.let {
                CardView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    radius = 8f // Radio de las esquinas
                    elevation = 4f // Elevación
                    setCardBackgroundColor(Color.WHITE) // Color de fondo
                }
            }

            // Crear TextView
            val textView = TextView(this).apply {
                text = element.link
                textSize = 16f // Tamaño de texto
                setTextColor(Color.BLUE) // Color de texto
                paint.isUnderlineText = true // Subrayar el texto
                setPadding(16, 16, 16, 16) // Espaciado interno
            }

            // Agregar el TextView a la CardView
            cardView?.addView(textView)
            cardView.setOnClickListener {
                binding.progressBar.visibility = View.VISIBLE
                downloadVideo(element)
            }

            binding.linearLayoutContainer.addView(cardView)

        }


    }


    fun downloadVideo(obj: EpisodeLink) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val lastDoIndex = obj.link.lastIndexOf(".")
                val fileExtension = obj.link.substring(lastDoIndex)

                val request = Request.Builder().url(obj.link)
                    .header("User-Agent", "Mozilla/5.0")  // Añadir User-Agent
                    .build()

                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) throw Exception("Error en la solicitud: $response")

                    val inputStream: InputStream? = response.body()?.byteStream()
                    val file = File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
//                        "video.mp4"
                        obj.title.plus(fileExtension)
                    )

                    inputStream?.let { input ->
                        FileOutputStream(file).use { output ->
                            val buffer = ByteArray(1024)
                            var bytesRead: Int
                            while (input.read(buffer).also { bytesRead = it } != -1) {
                                output.write(buffer, 0, bytesRead)
                            }
                            CoroutineScope(Dispatchers.Main).launch {
                                binding.progressBar.visibility = View.GONE
                                Log.d(
                                    "Download",
                                    "Video descargado correctamente en: ${file.absolutePath}"
                                )
                            }
                        }

                    }
                }
            } catch (e: Exception) {
                CoroutineScope(Dispatchers.Main).launch {
                    binding.progressBar.visibility = View.GONE
                    Log.e("Download", "Error al descargar el video: ${e.message}")
                }
            }
        }
    }

}