package com.example.mymovieapp.repositories

import com.example.mymovieapp.model.Movie
import retrofit2.Response

interface MovieRepository {
    suspend fun getMovieList(searchQuery: String): Response<Movie>
}