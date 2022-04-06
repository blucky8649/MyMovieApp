package com.example.mymovieapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymovieapp.model.Movie
import com.example.mymovieapp.repositories.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {
    private val _movieList:MutableStateFlow<Movie> = MutableStateFlow(Movie.emptyMovieItem)
    val movieList get(): StateFlow<Movie> = _movieList

    init {
        getMovieList()
    }

    fun getMovieList() = viewModelScope.launch {
        val response = repository.getMovieList("아이")
        _movieList.value = handleMovieResponse(response)
    }

    fun handleMovieResponse(respoonse: Response<Movie>): Movie {
        if (respoonse.isSuccessful) {
            respoonse.body().let { result ->
                return result!!
            }
        }
        return Movie.emptyMovieItem
    }
}