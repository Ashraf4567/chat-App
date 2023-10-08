package com.example.chatapp.ui.model

data class User(
    val id: String? = null,
    val userName: String? = null,
    val email: String? = null
) {
    companion object {
        const val collectionName = "users"
    }
}
