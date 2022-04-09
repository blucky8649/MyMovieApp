package com.example.mymovieapp.data

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.mymovieapp.model.Keyword
import com.example.mymovieapp.model.MovieItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MovieRepository {

    fun letMovieList(searchQuery: String): Flow<PagingData<MovieItem>>
    suspend fun insert(keyword: Keyword): Long
    fun getKeywords(): Flow<List<Keyword>>
    suspend fun deleteKeyword(keyword: Keyword)
    suspend fun clearKeywords()
}