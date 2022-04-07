package com.example.mymovieapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.mymovieapp.R
import com.example.mymovieapp.databinding.FragmentMovieWebviewBinding
import com.example.mymovieapp.databinding.FragmentSaveMovieBinding

class MovieWebViewFragment: Fragment(R.layout.fragment_movie_webview) {
    private var _binding: FragmentMovieWebviewBinding? = null
    val binding get() = _binding!!

    private val args: MovieWebViewFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieWebviewBinding.inflate(inflater, container, false)
        val movie = args.movieItem
        binding.webView.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            loadUrl(movie.link)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}