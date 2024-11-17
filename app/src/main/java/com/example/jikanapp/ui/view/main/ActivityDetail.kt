package com.example.jikanapp.ui.view.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import com.bumptech.glide.Glide
import com.example.jikanapp.databinding.ActivityDetailBinding
import com.example.jikanapp.domain.model.full.Anime
import com.example.jikanapp.ui.view.fragment.DetailFragment
import com.example.jikanapp.ui.view.fragment.ListFragment
import com.giphy.sdk.ui.pagination.GPHContent
import dagger.hilt.android.AndroidEntryPoint
import parcelable

@AndroidEntryPoint
class ActivityDetail : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding;

    /*Para usar el método no deprecado getParcella public <T> T getParcelableExtra(@Nullable String name, @NonNull Class<T> clazz) {
         throw new RuntimeException("Stub!");
     }*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            if (savedInstanceState == null) {
                val fragment = DetailFragment()
                val bundle = Bundle()
                val obj = intent.parcelable<Anime>(ListFragment.ANIME_OBJ)


//                obj?.let { setGifProperties(binding, it) }
//                binding.gifsGridView.isGone = true

                bundle.putParcelable(ListFragment.ANIME_OBJ, obj)
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction()
                    .replace(binding.containerFragment.id, fragment)
                    .commit()
            }
        } catch (e: Exception) {
            Log.e(
                this@ActivityDetail.javaClass.kotlin.simpleName,
                "onCreate " + "Error: " + e.toString()
            )
        }
    }

//    private fun setGifProperties(binding: ActivityDetailBinding, obj : Anime){
//        binding.gifsGridView.alpha = 0.5f
//        binding.gifsGridView.fixedSizeCells = true
//
//        if (obj != null) {
//            binding.gifsGridView.content = GPHContent.searchQuery(obj.title.toString())
//        }
//
//    }
}