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
    fun setSaveMode(isEnabled: Boolean) {
       viewModelScope.launch {
           userPref.setSaveSearchOption(isEnabled)
       }
    }
    var currentTime: Long = 0
    init {
        viewModelScope.launch {
            time.collectLatest {
                currentTime = it
            }
        }
    }
    val time = flow {
        while (true) {
            val currentTimeMills = System.currentTimeMillis()
            delay(1000L)
            emit(currentTimeMills)
        }
    }
    var list = ArrayList<Keyword>()
    val searchKeywords: Flow<MutableList<Keyword>> = userPref.searchDataFlow

    fun addSearchData(query: String) {
        if (!stateOfSaveOption) return
        list.add(Keyword(currentTime, query))
        setSearchData(list)
    }

    fun removeSearchData(pos: Int) {
        list.removeAt(pos)
        setSearchData(list)
    }
    fun clearSearchData() {
        viewModelScope.launch {
            list.clear()
            userPref.setSearchData(list)
        }

    }
    fun setSearchData(arr: MutableList<Keyword>) {
        viewModelScope.launch {
            Log.d("Lee", "$arr")
            userPref.setSearchData(arr)
            delay(300L)
        }
    }
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