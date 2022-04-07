package com.example.mymovieapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.mymovieapp.api.MovieApi
import com.example.mymovieapp.model.MovieItem
import com.example.mymovieapp.util.Constants.PAGE_SIZE
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalPagingApi::class)
class MovieRepositoryImpl(
    private val api: MovieApi
): MovieRepository {
    fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false)
    }

    override fun letMovieList(searchQuery: String): Flow<PagingData<MovieItem>> {
        return Pager(
            config = getDefaultPageConfig(),
            pagingSourceFactory = { MovieListPagingSource(api, searchQuery) }
        ).flow
    }

}