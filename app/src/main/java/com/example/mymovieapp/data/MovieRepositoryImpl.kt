package com.example.mymovieapp.data

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.mymovieapp.api.MovieApi
import com.example.mymovieapp.db.HistoryDatabase
import com.example.mymovieapp.model.Keyword
import com.example.mymovieapp.model.MovieItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalPagingApi::class)
class MovieRepositoryImpl(
    private val api: MovieApi,
    private val db: HistoryDatabase
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
    // DB 트랜잭션
    override suspend fun insert(keyword: Keyword) = db.getHistoryDao().insert(keyword)
    override fun getKeywords(): Flow<List<Keyword>> = db.getHistoryDao().getAllKeywords()
    override suspend fun deleteKeyword(keyword: Keyword) = db.getHistoryDao().deleteKeyword(keyword)
    override suspend fun clearKeywords() = db.getHistoryDao().clearKeywords()
}