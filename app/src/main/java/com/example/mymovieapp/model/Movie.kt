package com.example.mymovieapp.model

data class Movie(
    val lastBuildDate: String,
    val total: Int = 0,
    val start: Int = 0,
    val display: Int = 0,
    val items: List<MovieItem>
)
