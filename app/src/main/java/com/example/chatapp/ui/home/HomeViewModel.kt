package com.example.chatapp.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.common.SingleLiveEvent
import com.example.chatapp.ui.Message
import com.example.chatapp.ui.SessionProvider
import com.example.chatapp.ui.firestore.RoomsDao
import com.example.chatapp.ui.model.Room
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeViewModel : ViewModel() {

    val event = SingleLiveEvent<HomeViewEvent>()
    val roomsLiveData = MutableLiveData<List<Room>>()

    var loadingLiveData = MutableLiveData<Message?>()
    val messageLiveData = SingleLiveEvent<Message>()


    fun navigateToAddRoom() {
        event.postValue(
            HomeViewEvent.NavigateToAddRoom
        )
    }

    fun loadRooms() {
        RoomsDao.getAllRooms() { task ->
            if (!task.isSuccessful) {
                //show message
                return@getAllRooms
            }
            val rooms = task.result.toObjects(Room::class.java)
            roomsLiveData.postValue(rooms)
        }
    }

    fun logout() {
        messageLiveData.postValue(
            Message(
                message = "Are you sure you want logout?",
                positiveActionName = "yes",
                onPositiveActionClick = {
                    Firebase.auth.signOut()
                    SessionProvider.user = null
                    event.postValue(
                        HomeViewEvent.NavigateToLogIn
                    )
                },
                negativeActionName = "cancel"
            )
        )

    }


}