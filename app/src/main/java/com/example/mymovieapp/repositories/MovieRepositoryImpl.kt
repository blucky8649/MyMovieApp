package com.example.mymovieapp.repositories

import com.example.mymovieapp.api.MovieApi
import com.example.mymovieapp.model.Movie
import retrofit2.Response
import com.example.mymovieapp.Constants.NAVER_ID
import com.example.mymovieapp.Constants.NAVER_SECRET
import com.example.mymovieapp.Constants.PAGE_SIZE
import com.example.mymovieapp.Constants.START_PAGE

class MovieRepositoryImpl(
    private val api: MovieApi
): MovieRepository {
    override suspend fun getMovieList(searchQuery: String): Response<Movie> {
        return api.getMovieList(NAVER_ID, NAVER_SECRET, searchQuery, PAGE_SIZE, START_PAGE)
    }
}