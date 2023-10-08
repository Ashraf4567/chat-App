package com.example.chatapp.ui.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.common.SingleLiveEvent
import com.example.chatapp.ui.SessionProvider
import com.example.chatapp.ui.firestore.MessagesDao
import com.example.chatapp.ui.model.Message
import com.example.chatapp.ui.model.Room
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener

class ChatViewModel : ViewModel() {

    val messageLiveData = MutableLiveData<String>()
    val toastLiveData = SingleLiveEvent<String>()
    val newMessagesLiveData = SingleLiveEvent<List<Message>>()
    private var room: Room? = null

    fun changeRoom(room: Room?) {
        this.room = room
        listenForMessagesInRoom()
    }

    fun sendMessage() {
        if (messageLiveData.value.isNullOrBlank()) return

        val message = Message(
            content = messageLiveData.value,
            dateTime = Timestamp.now(),
            senderName = SessionProvider.user?.userName,
            senderId = SessionProvider.user?.id,
            roomId = room?.id
        )
        MessagesDao.sendMessage(message) { task ->
            if (task.isSuccessful) {
                messageLiveData.value = ""
                return@sendMessage
            }
            toastLiveData.postValue("something went wong")
        }
    }

    fun listenForMessagesInRoom() {
        MessagesDao.getMessagesCollection(room?.id ?: "")
            .orderBy("dateTime")
            .limitToLast(100)
            .addSnapshotListener(EventListener { snapShot, error ->
                var newMessages = mutableListOf<Message>()
                snapShot?.documentChanges?.forEach { docChange ->
                    if (docChange.type == DocumentChange.Type.ADDED) {
                        val message = docChange.document.toObject(Message::class.java)
                        newMessages.add(message)
                    }
                }
                newMessagesLiveData.postValue(newMessages)

            })
    }
}