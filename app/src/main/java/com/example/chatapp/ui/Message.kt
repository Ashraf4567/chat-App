package com.example.chatapp.ui

data class Message(
    val message: String? = null,
    val positiveActionName: String? = null,
    val negativeActionName: String? = null,
    val onPositiveActionClick: OnDialogActionClick? = null,
    val onNegativeActionClick: OnDialogActionClick? = null,
    val isCancelable: Boolean = true
)

fun interface OnDialogActionClick {
    fun onActionClick()
}
