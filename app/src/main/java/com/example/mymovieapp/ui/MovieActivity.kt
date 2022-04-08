package com.example.mymovieapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.paging.ExperimentalPagingApi
import com.example.mymovieapp.R
import com.example.mymovieapp.databinding.ActivityMovieBinding
import com.example.mymovieapp.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
@ExperimentalPagingApi
class MovieActivity : AppCompatActivity() {
    val viewModel: MovieViewModel by viewModels()
    private val binding: ActivityMovieBinding by lazy {
        ActivityMovieBinding.inflate(layoutInflater)
    }
    val navHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.movieNavHostFragment) as NavHostFragment
    }
    val navController by lazy {
        navHostFragment.findNavController()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
    override fun onSupportNavigateUp(): Boolean = navController.navigateUp()
}