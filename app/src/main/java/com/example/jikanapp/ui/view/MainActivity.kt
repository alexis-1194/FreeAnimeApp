package com.example.jikanapp.ui.view

//Para verificar los codecs
import android.content.Context
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.jikanapp.databinding.ActivityMainBinding
import com.example.jikanapp.ui.view.fragment.AnimeFragment
import com.example.jikanapp.ui.view.main.ActivityList
import com.giphy.sdk.ui.Giphy
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val client = OkHttpClient()

    private val YOUR_ANDROID_SDK_KEY = "LrbNPCFc4QL15wDRza3YrYDETAXbsceS"

    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar Giphy
        Giphy.configure(this, YOUR_ANDROID_SDK_KEY)
//        GiphyDialogFragment.newInstance().show(supportFragmentManager, "giphy_dialog")


//         Cargar el Fragment dentro del contenedor
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.containerFragment.id, AnimeFragment())
                .commit()
        }

        binding.buscarBtn.setOnClickListener {
            val name = binding.buscarEdit.text.toString()
            cargarDatosAnime(name)
        }


//        startVideoExoPlayer()

        //El problema raiz parece ser que al descargar el video,
        //este no se puede reproducir debido al formato que no es admitido

        val urlVideo = "https://cdnplaypro.com/e/kbhra8a21yth"

        // Descargar el archivo de video
//        downloadVideo("https://cdnplaypro.com/e/kbhra8a21yth","C:/Users/Alexis/Desktop/AndroidApps/JikanApp2/app/src/main/java/com/example/jikanapp/video")
        val lista: List<File>
//        lista = obtenerArchivosMp4(this)
//        playVideo(lista.get(0))
//
//        val videoPath = "/storage/emulated/0/Android/data/com.example.jikanapp/files/Download/video.mp4"
//        getVideoCodecInfo(videoPath)

//        val request = DownloadManager.Request(Uri.parse(urlVideo))
//            .setMimeType("video/mp4")
//            .setTitle("File")
//            .setDescription("Downloading")
//            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//            .setAllowedOverMetered(true)
//            // Establece el nombre del archivo con extensión .mp4
//
//        val dm = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
//
//
//        dm.enqueue(request)

    }





    private fun cargarDatosAnime(nombreAnime: String) {
        val intent = Intent(this, ActivityList::class.java)
        intent.putExtra(NOMBRE_ANIME, nombreAnime)
        startActivity(intent)
    }

    companion object {
        val NOMBRE_ANIME: String = "nombre_anime"
    }

//    private fun startVideoExo1er(){
//        // Inicializa PlayerView
//        playerView = binding.playerView
//
//        // Inicializa ExoPlayer
//        player = ExoPlayer.Builder(this).build()
//        playerView.player = player
//
//        // Configura la fuente del video con la URL
//        val videoUrl = "https://ww4.video-content-cdn.com/www119.anzeat.pro/streamhls/0b594d900f47daabc194844092384914/ep.1121.1728181819.m3u8.php?fallback=aHR0cHM6Ly9zM3Rha3UuY29tL2FicGwxMjQ1P2lkPU1qTTBNems1JnRpdGxlPU9uZStQaWVjZStFcGlzb2RlKzExMjEmdHlwZXN1Yj1TVUI=&error=aHR0cHM6Ly9hbmltZW5zaW9uLnRvL3B1YmxpYy1hcGkvZXJyb3JfcmVwb3J0aW5nLnBocD9pZD0zMzA2MzQ3ODgw"
//
//        val mediaItem = MediaItem.fromUri(videoUrl)
//        player.setMediaItem(mediaItem)
//
//        // Prepara y reproduce el video
//        player.prepare()
//        player.playWhenReady = true
//    }

    private fun downloadVideo(url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = Request.Builder()
                    .url(url)
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

                        // Cambia al hilo principal para reproducir el video
                        withContext(Dispatchers.Main) {
                            playVideo(file)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("Download", "Error al descargar el video: ${e.message}")
            }
        }
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

    fun mostrarContenidoDescargasApp(context: Context) {
        // Ruta a la carpeta de descargas específica de la app
        val rutaDescargasApp = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)

        rutaDescargasApp?.let {
            if (it.exists() && it.isDirectory) {
                val archivos = it.listFiles()
                archivos?.forEach { archivo ->
                    Log.d(
                        "ContenidoDescargasApp",
                        "Archivo: ${archivo.name} - Es directorio: ${archivo.isDirectory}"
                    )
                }
            } else {
                Log.d(
                    "ContenidoDescargasApp",
                    "La carpeta de descargas específica de la app no existe o no es un directorio"
                )
            }
        }
    }

//    private fun downloadVideo(url: String, directoryPath: String) {
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val request = Request.Builder()
//                    .url(url)
//                    .build()
//
//                client.newCall(request).execute().use { response ->
//                    if (!response.isSuccessful) throw Exception("Error en la solicitud: $response")
//
//                    // Crear el directorio si no existe
//                    val directory = File(directoryPath)
//                    if (!directory.exists()) {
//                        directory.mkdirs()  // Crea los directorios necesarios
//                    }
//
//                    // Guardar el archivo descargado en el almacenamiento local
//                    val inputStream: InputStream? = response.body()?.byteStream()
//                    val file = File(
//                        directory,
//                        "video.mp4"
//                    )  // Cambia aquí el nombre del archivo si lo deseas
//
//                    inputStream?.let { input ->
//                        val outputStream = FileOutputStream(file)
//                        val buffer = ByteArray(1024)
//                        var bytesRead: Int
//                        while (input.read(buffer).also { bytesRead = it } != -1) {
//                            outputStream.write(buffer, 0, bytesRead)
//                        }
//                        outputStream.close()
//                        Log.d("Download", "Video descargado correctamente en: ${file.absolutePath}")
//
//                        // Mover a hilo principal para reproducir el video
//                        withContext(Dispatchers.Main) {
//                            playVideo(file)  // Esta llamada ahora se hace en el hilo principal
//                        }
//                    }
//                }
//            } catch (e: Exception) {
//                Log.e("Download", "Error : ${e.message}")
//            }
//        }
//    }

//    private fun downloadVideo2(url: String) {
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val request = Request.Builder()
//                    .url(url)
//                    .build()
//
//                client.newCall(request).execute().use { response ->
//                    if (!response.isSuccessful) throw Exception("Error en la solicitud: $response")
//
//                    // Guardar el archivo descargado en el almacenamiento local
//                    val inputStream: InputStream? = response.body()?.byteStream()
//                    val file =
//                        File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "video.mp4")
//
//                    inputStream?.let { input ->
//                        val outputStream = FileOutputStream(file)
//                        val buffer = ByteArray(1024)
//                        var bytesRead: Int
//                        while (input.read(buffer).also { bytesRead = it } != -1) {
//                            outputStream.write(buffer, 0, bytesRead)
//                        }
//                        outputStream.close()
//                        Log.d("Download", "Video descargado correctamente en: ${file.absolutePath}")
//
//                        // Mover a hilo principal para reproducir el video
//                        withContext(Dispatchers.Main) {
//                            playVideoWithMediaPlayer(file.absolutePath)  // Esta llamada ahora se hace en el hilo principal
//                        }
//                    }
//                }
//            } catch (e: Exception) {
//                Log.e("Download", "Error al descargar el video: ${e.message}")
//            }
//        }
//    }

//    private fun playVideo(url: String) {
//        // Inicializa PlayerView
//        playerView = binding.playerView
//
//        // Inicializa ExoPlayer
//        player = ExoPlayer.Builder(this).build()
//        playerView.player = player
//
//        // Configura la fuente del video con la URL
//
//        val mediaItem = MediaItem.fromUri(url)
//        player.setMediaItem(mediaItem)
//
//        // Prepara y reproduce el video
//        player.prepare()
//        player.playWhenReady = true
//    }


    private fun playVideo(file: File) {
//        val videoView = binding.playerView
//        val videoUri = Uri.fromFile(file)  // Convertimos el archivo en una URI
//        videoView.setVideoURI(videoUri)
//        videoView.start()  // Reproducir el video
    }

//    private fun playVideoWithMediaPlayer(videoPath: String) {
//        val mediaPlayer = MediaPlayer()
//        mediaPlayer.setDataSource(this, Uri.parse(videoPath))
//        mediaPlayer.prepare()
//        mediaPlayer.start()
//    }

    private fun getVideoCodecInfo(videoPath: String) {
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(videoPath)
            val codec = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE)
            val width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
            val height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)

            Log.d("Video Info", "Codec: $codec")
            Log.d("Video Info", "Width: $width")
            Log.d("Video Info", "Height: $height")
        } catch (e: Exception) {
            Log.e("Video Info", "Error al obtener la información del video: ${e.message}")
        } finally {
            retriever.release()
        }
    }
}