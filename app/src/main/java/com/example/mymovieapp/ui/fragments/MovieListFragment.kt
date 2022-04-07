package com.example.mymovieapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymovieapp.util.Constants.DELEY_500L
import com.example.mymovieapp.R
import com.example.mymovieapp.adapter.LoaderStateAdapter
import com.example.mymovieapp.adapter.MovieListAdapter
import com.example.mymovieapp.databinding.FragmentMovieListBinding
import com.example.mymovieapp.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
@ExperimentalPagingApi
class MovieListFragment: Fragment(R.layout.fragment_movie_list) {
    private var _binding: FragmentMovieListBinding? = null
    val binding get() = _binding!!

    lateinit var movieAdapter: MovieListAdapter
    lateinit var loadStateAdapter: LoaderStateAdapter
    val viewModel: MovieViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        init()
        setupRecyclerView()
        fetchMovieList()

        var job: Job? = null
        binding.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(DELEY_500L)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.postKeyword(editable.toString())
                    }
                }
            }
        }

        return binding.root
    }
    fun init() {
        movieAdapter = MovieListAdapter()
        loadStateAdapter = LoaderStateAdapter { movieAdapter.retry() }
    }
    fun fetchMovieList() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.result
                .collectLatest { movie ->
                    movieAdapter.submitData(viewLifecycleOwner.lifecycle, movie)
                }
        }
    }
    fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = movieAdapter.withLoadStateFooter(loadStateAdapter)
            layoutManager = LinearLayoutManager(requireContext())

        }

        movieAdapter.setOnItemClickListener { item ->
            val action = MovieListFragmentDirections.actionMovieListFragmentToMovieWebViewFragment(item)
            findNavController().navigate(action)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}