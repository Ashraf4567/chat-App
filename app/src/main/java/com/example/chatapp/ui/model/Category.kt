package com.example.chatapp.ui.model

import com.example.chatapp.R

data class Category(
    val id: Int,
    val title: String,
    val imageResId: Int
) {
    companion object {
        fun getCategories() = listOf<Category>(
            Category(1, "Sports", R.drawable.sports),
            Category(2, "Movies", R.drawable.movies),
            Category(3, "Music", R.drawable.music),
        )

        fun getCategoryImageByCategoryId(catId: Int?): Int {
            return when (catId) {
                1 -> R.drawable.sports
                2 -> R.drawable.movies
                3 -> R.drawable.music
                else -> {
                    R.drawable.sports
                }
            }
        }
    }
}