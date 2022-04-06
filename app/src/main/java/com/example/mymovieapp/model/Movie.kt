package com.example.mymovieapp.model

data class Movie(
    val lastBuildDate: String,
    val total: Int = 0,
    val start: Int = 0,
    val display: Int = 0,
    val items: List<MovieItem>
) {
    companion object {
        val emptyMovieItem = Movie(
            "",
            0,
            0,
            0,
            emptyList()
        )
    }
}
