package com.example.chatapp.ui.splash

import androidx.lifecycle.ViewModel
import com.example.chatapp.common.SingleLiveEvent
import com.example.chatapp.ui.SessionProvider
import com.example.chatapp.ui.firestore.UsersDao
import com.example.chatapp.ui.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashViewModel : ViewModel() {

    val event = SingleLiveEvent<SplashViewEvent>()

    fun redirect() {
        if (Firebase.auth.currentUser == null) {
            event.postValue(
                SplashViewEvent.NavigateToLogin
            )
            return
        }
        UsersDao.getUser(
            Firebase.auth.currentUser?.uid
        ) { task ->
            if (!task.isSuccessful) {
                event.postValue(
                    SplashViewEvent.NavigateToLogin
                )
                return@getUser
            }
            val user = task.result.toObject(User::class.java)
            SessionProvider.user = user
            event.postValue(
                SplashViewEvent.NavigateToHome
            )

        }
    }
}