package com.example.mymovieapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mymovieapp.R
import com.example.mymovieapp.databinding.FragmentMovieListBinding
import com.example.mymovieapp.databinding.FragmentSaveMovieBinding

class MovieListFragment: Fragment(R.layout.fragment_movie_list) {
    private var _binding: FragmentMovieListBinding? = null
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}