package com.example.mymovieapp.model

data class MovieItem(
    val title: String,
    val link: String,
    val image: String,
    val subtitle: String,
    val pubDate: String,
    val director: String,
    val actor: String,
    val userRating: Double = 0.0,
) {
    companion object {
        val emptyMovieItem = MovieItem(
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            0.0,
        )
    }

}
