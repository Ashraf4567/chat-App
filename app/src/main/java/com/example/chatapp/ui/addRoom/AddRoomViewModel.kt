package com.example.chatapp.ui.addRoom

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.common.SingleLiveEvent
import com.example.chatapp.ui.Message
import com.example.chatapp.ui.SessionProvider
import com.example.chatapp.ui.firestore.RoomsDao
import com.example.chatapp.ui.model.Category

class AddRoomViewModel : ViewModel() {

    val events = SingleLiveEvent<AddRoomViewEvent>()

    val roomName = MutableLiveData<String>()
    val roomDesc = MutableLiveData<String>()
    val roomNameError = MutableLiveData<String?>()
    val roomDescError = MutableLiveData<String?>()

    val categories = Category.getCategories()
    private var selectedCategory = categories[0]
    val messageLiveData = SingleLiveEvent<Message>()
    val loadingLiveData = MutableLiveData<Message?>()

    fun createRoom() {
        if (!isValid()) return

        loadingLiveData.postValue(
            Message(
                message = "loading...",
                isCancelable = false
            )
        )

        RoomsDao.createRoom(
            title = roomName.value ?: "",
            desc = roomDesc.value ?: "",
            ownerId = SessionProvider.user?.id ?: "",
            categoryId = selectedCategory.id
        ) { task ->
            loadingLiveData.postValue(null)
            if (task.isSuccessful) {
                messageLiveData.postValue(
                    Message(
                        message = "room created successfully",
                        positiveActionName = "ok",
                        onPositiveActionClick = {
                            events.postValue(
                                AddRoomViewEvent.NavigateToHomeAndFinish
                            )
                        }
                    )
                )
            } else {

            }

        }

    }

    private fun isValid(): Boolean {
        var valid = true
        if (roomName.value.isNullOrBlank()) {
            roomNameError.postValue("please Enter a room name")
            valid = false
        } else {
            roomNameError.postValue(null)
        }

        if (roomDesc.value.isNullOrBlank()) {
            roomDescError.postValue("please enter a room description")
            valid = false
        } else {
            roomDescError.postValue(null)
        }
        return valid
    }

    fun onCategorySelected(position: Int) {
        selectedCategory = categories[position]
    }

}