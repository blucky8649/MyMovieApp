package com.example.mymovieapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.mymovieapp.data.MovieRepository
import com.example.mymovieapp.model.MovieItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
@ExperimentalPagingApi
@ExperimentalCoroutinesApi
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _searchQuery:MutableStateFlow<String> = MutableStateFlow("")
    val searchQuery get(): StateFlow<String> = _searchQuery

    fun postKeyword(searchQuery: String) {
        _searchQuery.value = searchQuery
    }
    val result = searchQuery.flatMapLatest {
        Log.d("Lee", it)
        repository.letMovieList(it).cachedIn(viewModelScope)
    }


}