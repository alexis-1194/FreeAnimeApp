package com.example.jikanapp.ui.view.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.jikanapp.databinding.ActivityListBinding
import com.example.jikanapp.ui.view.MainActivity
import com.example.jikanapp.ui.view.fragment.AnimeFragment
import com.example.jikanapp.ui.view.fragment.ListFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ActivityList : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            if (savedInstanceState == null) {
                val fragment = ListFragment()
                val bundle = Bundle()
                val nombreAnime = intent.getStringExtra(MainActivity.NOMBRE_ANIME)
                bundle.putString(MainActivity.NOMBRE_ANIME, nombreAnime)
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction()
                    .replace(binding.containerFragment.id, fragment)
                    .commit()
            }
        } catch (e: Exception) {
            Log.e(
                Thread.currentThread().stackTrace[1].className,
                Thread.currentThread().stackTrace[1].methodName + "Error: " + e.toString()
            )
        }
        // Cargar el Fragment dentro del contenedor

    }
}