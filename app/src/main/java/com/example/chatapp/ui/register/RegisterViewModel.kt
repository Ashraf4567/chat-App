package com.example.chatapp.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.common.SingleLiveEvent
import com.example.chatapp.ui.Message
import com.example.chatapp.ui.SessionProvider
import com.example.chatapp.ui.firestore.UsersDao
import com.example.chatapp.ui.model.User
import com.google.firebase.auth.FirebaseAuth

class RegisterViewModel : ViewModel() {

    val messageLiveData = SingleLiveEvent<Message>()

    val userName = MutableLiveData<String>("Ashraf Mohamed")
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val passwordConfirmation = MutableLiveData<String>()

    var isLoading = MutableLiveData<Boolean>()

    val userNameError = MutableLiveData<String?>()
    val emailError = MutableLiveData<String?>()
    val passwordError = MutableLiveData<String?>()
    val passwordConfirmationError = MutableLiveData<String?>()

    val events = SingleLiveEvent<RegisterViewEvent>()

    private val auth = FirebaseAuth.getInstance()

    fun register() {
        if (!validForm()) return

        isLoading.value = true
        auth.createUserWithEmailAndPassword(
            email.value!!, password.value!!
        ).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                insertUserToFireStore(task.result.user?.uid)
                isLoading.value = false
//                errorLiveData.postValue(
//                    ViewError(
//                        message = task.result.user?.uid
//                    )
//                )

            } else {


                messageLiveData.postValue(
                    Message(
                        message = task.exception?.localizedMessage
                    )
                )
                isLoading.value = false
            }

        }
    }

    private fun insertUserToFireStore(uid: String?) {
        val user = User(
            id = uid,
            userName = userName.value,
            email = email.value
        )
        UsersDao.createUser(user) { task ->
            if (task.isSuccessful) {
                messageLiveData.postValue(
                    Message(
                        message = "User registered Successfully",
                        positiveActionName = "ok",
                        onPositiveActionClick = {
                            //save user
                            SessionProvider.user = user
                            //navigate to home
                            events.postValue(RegisterViewEvent.NavigateToHome)

                        }
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
        if (userName.value.isNullOrBlank()) {
            //showError
            userNameError.postValue("please enter user name")
            isValid = false
        } else {
            userNameError.postValue(null)
        }
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
        if (passwordConfirmation.value.isNullOrBlank() ||
            passwordConfirmation.value != password.value
        ) {
            //showError
            passwordConfirmationError.postValue("passwords doesn't match")
            isValid = false
        } else {
            passwordConfirmationError.postValue(null)
        }
        return isValid
    }

    fun navigateToLogin() {
        events.postValue(RegisterViewEvent.NavigateToLogin)
    }
}