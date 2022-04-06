package com.example.mymovieapp.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mymovieapp.util.Constants
import com.example.mymovieapp.util.Constants.START_PAGE
import com.example.mymovieapp.api.MovieApi
import com.example.mymovieapp.model.MovieItem
import com.example.mymovieapp.util.Constants.PAGE_SIZE
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class MovieListPagingSource(val movieApi: MovieApi, val searchQuery: String) : PagingSource<Int, MovieItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieItem> {
        return try {
            val page = params.key ?: 1
            val response = movieApi.getMovieList(
                Constants.NAVER_ID,
                Constants.NAVER_SECRET, searchQuery,
                Constants.PAGE_SIZE, page)

            Log.d("Lee", page.toString() + " " + response.total)
            LoadResult.Page(
                response.items,
                prevKey = null,
                // 다음에 더 이상 페이지가 없다면 null 리턴
                nextKey = if (page + PAGE_SIZE >= response.total) null else page + PAGE_SIZE
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, MovieItem>): Int? {
        return state.anchorPosition
    }
}