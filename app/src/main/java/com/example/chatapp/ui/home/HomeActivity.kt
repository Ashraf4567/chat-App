package com.example.chatapp.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.chatapp.R
import com.example.chatapp.databinding.ActivityHomeBinding
import com.example.chatapp.ui.Constants
import com.example.chatapp.ui.addRoom.AddRoomActivity
import com.example.chatapp.ui.chat.ChatActivity
import com.example.chatapp.ui.login.LoginActivity
import com.example.chatapp.ui.model.Room
import com.example.chatapp.ui.showLoadingProgressDialog
import com.example.chatapp.ui.showMessage

class HomeActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        initViews()
        subscribeToLiveData()
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadRooms()
    }

    private var loadingDialog: AlertDialog? = null
    private fun subscribeToLiveData() {
        viewModel.event.observe(this, ::handleEvent)
        viewModel.roomsLiveData.observe(this) {
            adapter.changeData(it)
        }

        viewModel.messageLiveData.observe(this) {
            showMessage(
                message = it.message ?: "",
                posActionName = it.positiveActionName,
                posAction = it.onPositiveActionClick,
                nagActionName = it.negativeActionName,
                negAction = it.onNegativeActionClick,
                isCancelable = true
            )
        }

        viewModel.loadingLiveData.observe(this) {
            if (it == null) {
                //hide
                loadingDialog?.dismiss()
                loadingDialog = null
                return@observe
            }
            //show
            loadingDialog = showLoadingProgressDialog(it.message ?: "", it.isCancelable)
            loadingDialog?.show()
        }
    }

    private fun handleEvent(homeViewEvent: HomeViewEvent?) {
        when (homeViewEvent) {
            HomeViewEvent.NavigateToAddRoom -> {
                navigateToAddRoom()

            }

            HomeViewEvent.NavigateToLogIn -> {
                navigateToLogin()
                finish()
            }

            else -> {}
        }
    }

    private fun navigateToAddRoom() {
        val intent = Intent(this, AddRoomActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

    }

    val adapter = RoomsRecyclerAdapter()
    private fun initViews() {

        viewBinding.lifecycleOwner = this
        viewBinding.vm = viewModel
        viewBinding.content.roomsRecycler.adapter = adapter

        adapter.onItemClickListener = RoomsRecyclerAdapter.OnItemClickListener { position, room ->
            navigateToRoom(room)
        }
    }

    private fun navigateToRoom(room: Room) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra(Constants.EXTRA_ROOM, room)
        startActivity(intent)
    }
}