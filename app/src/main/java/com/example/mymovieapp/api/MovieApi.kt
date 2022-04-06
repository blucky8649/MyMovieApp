package com.example.mymovieapp.api

import com.example.mymovieapp.model.Movie
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface MovieApi {

    @GET("v1/search/movie.json")
    suspend fun getMovieList(
        @Header("X-Naver-Client-Id")
        clientId: String,
        @Header("X-Naver-Client-Secret")
        clientSecret: String,
        @Query("query")
        query: String,
        @Query("display")
        display: Int? = null,
        @Query("start")
        start: Int? = null
    ): Movie
}
