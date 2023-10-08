package com.example.chatapp.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.common.SingleLiveEvent
import com.example.chatapp.ui.Message
import com.example.chatapp.ui.SessionProvider
import com.example.chatapp.ui.firestore.UsersDao
import com.example.chatapp.ui.model.User
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {

    val email = MutableLiveData<String>("ashraf@app.com")
    val password = MutableLiveData<String>()

    val emailError = MutableLiveData<String?>()
    val passwordError = MutableLiveData<String?>()

    val messageLiveData = SingleLiveEvent<Message>()

    var isLoading = MutableLiveData<Boolean>()
    val auth = FirebaseAuth.getInstance()
    val events = SingleLiveEvent<LoginViewEvent>()

    fun login() {
        if (!validForm()) return
        isLoading.value = true

        auth.signInWithEmailAndPassword(email.value!!, password.value!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    getUserFromFirestore(task.result.user?.uid)
                } else {
                    messageLiveData.postValue(
                        Message(
                            message = task.exception?.localizedMessage
                        )
                    )
                }
            }

    }

    private fun getUserFromFirestore(uid: String?) {
        UsersDao.getUser(uid) { task ->
            isLoading.value = false
            if (task.isSuccessful) {
                val user = task.result.toObject(User::class.java)
                SessionProvider.user = user
                messageLiveData.postValue(
                    Message(
                        message = "logged in successfully",
                        positiveActionName = "ok",
                        onPositiveActionClick = {
                            events.postValue(LoginViewEvent.NavigateToHome)
                        }, isCancelable = false
                    )
                )
            } else {
                messageLiveData.postValue(
                    Message(
                        message = task.exception?.localizedMessage
                    )
                )

            }

        }
    }

    private fun validForm(): Boolean {
        var isValid = true

        if (email.value.isNullOrBlank()) {
            //showError
            emailError.postValue("please enter email")
            isValid = false
        } else {
            emailError.postValue(null)
        }
        if (password.value.isNullOrBlank()) {
            //showError
            passwordError.postValue("please enter password")
            isValid = false
        } else {
            passwordError.postValue(null)
        }

        return isValid
    }

    fun navigateToRegister() {
        events.postValue(LoginViewEvent.NavigateToRegister)
    }

}