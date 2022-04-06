package com.example.mymovieapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mymovieapp.R
import com.example.mymovieapp.databinding.ActivityMovieBinding.inflate
import com.example.mymovieapp.databinding.FragmentSaveMovieBinding
import com.example.mymovieapp.databinding.FragmentSearchKeywordsBinding

class SearchKeywordFragment: Fragment(R.layout.fragment_search_keywords) {
    private var _binding: FragmentSearchKeywordsBinding? = null
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchKeywordsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}