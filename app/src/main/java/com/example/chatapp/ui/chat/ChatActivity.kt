package com.example.chatapp.ui.chat

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.R
import com.example.chatapp.databinding.ActivityChatBinding
import com.example.chatapp.ui.Constants
import com.example.chatapp.ui.model.Room

class ChatActivity : AppCompatActivity() {
    val viewModel: ChatViewModel by viewModels()
    private lateinit var viewBinding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        initParams()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.toastLiveData.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        viewModel.newMessagesLiveData.observe(this) {
            messagesAdapter.addNewMessage(it)
            viewBinding.content.messagesRecycler.smoothScrollToPosition(
                messagesAdapter.itemCount
            )
        }
    }

    private fun initParams() {
        var room = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Constants.EXTRA_ROOM, Room::class.java)
        } else {
            intent.getParcelableExtra(Constants.EXTRA_ROOM) as Room?
        }

        viewModel.changeRoom(room)
    }

    val messagesAdapter = MessagesAdapter(mutableListOf())
    private fun initViews() {
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat)
        viewBinding.lifecycleOwner = this
        viewBinding.vm = viewModel

        viewBinding.content.messagesRecycler.adapter = messagesAdapter
        (viewBinding.content.messagesRecycler.layoutManager as LinearLayoutManager).stackFromEnd =
            true
    }
}