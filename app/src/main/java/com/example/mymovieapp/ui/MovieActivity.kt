package com.example.mymovieapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.mymovieapp.R
import com.example.mymovieapp.databinding.ActivityMovieBinding
import com.example.mymovieapp.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieActivity : AppCompatActivity() {
    val viewModel: MovieViewModel by viewModels()
    private lateinit var binding: ActivityMovieBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.movieNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        lifecycleScope.launch {
            viewModel.movieList.collectLatest {
                Log.d("Lee", it.toString())
            }
        }

    }
}