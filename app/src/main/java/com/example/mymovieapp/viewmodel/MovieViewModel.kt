package com.example.mymovieapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import com.example.mymovieapp.data.MovieRepository
import com.example.mymovieapp.model.Keyword
import com.example.mymovieapp.util.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@ExperimentalPagingApi
@ExperimentalCoroutinesApi
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val userPref: UserPreferences
) : ViewModel() {
    var stateOfSaveOption = false
    val isSaveEnabled = userPref.saveOptionFlow.map {
        stateOfSaveOption = it
        it
    }

    private val _searchViewState:MutableStateFlow<Boolean> = MutableStateFlow(false)
    val searchViewState get(): StateFlow<Boolean> = _searchViewState

    fun setSearchViewVisibility(isVisible: Boolean) {
        _searchViewState.value = isVisible
    }

    fun setSaveMode(isEnabled: Boolean) {
       viewModelScope.launch {
           userPref.setSaveSearchOption(isEnabled)
       }
    }

    fun addSearchData(keyword: Keyword) = viewModelScope.launch {
        if (!stateOfSaveOption) return@launch
        repository.insert(keyword)
    }
    fun getSavedKeywords() = repository.getKeywords()
    fun deleteKeyword(keyword: Keyword) = viewModelScope.launch {
        repository.deleteKeyword(keyword)
    }
    fun clearKeywords() = viewModelScope.launch {
        repository.clearKeywords()
    }
    private val _searchQuery:MutableStateFlow<String> = MutableStateFlow("플로우")
    val searchQuery get(): StateFlow<String> = _searchQuery

    fun postKeyword(searchQuery: String) {
        _searchQuery.value = searchQuery
    }
    val result = searchQuery.flatMapLatest {
        Log.d("Lee", it)
        repository.letMovieList(it).cachedIn(viewModelScope)
    }
}