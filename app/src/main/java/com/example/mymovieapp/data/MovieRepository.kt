package com.example.mymovieapp.data

import androidx.paging.PagingData
import com.example.mymovieapp.model.MovieItem
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun letMovieList(searchQuery: String): Flow<PagingData<MovieItem>>
}