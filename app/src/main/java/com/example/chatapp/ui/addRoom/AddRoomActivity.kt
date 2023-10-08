package com.example.chatapp.ui.addRoom

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.chatapp.R
import com.example.chatapp.databinding.ActivityAddRoomBinding
import com.example.chatapp.ui.home.HomeActivity
import com.example.chatapp.ui.showLoadingProgressDialog
import com.example.chatapp.ui.showMessage

class AddRoomActivity : AppCompatActivity() {

    lateinit var viewBinding: ActivityAddRoomBinding
    val viewModel: AddRoomViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_room)
        initViews()
        subscribeToLiveData()
    }

    private var loadingDialog: AlertDialog? = null
    private fun subscribeToLiveData() {

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

        viewModel.events.observe(this, ::handleEvents)

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

    private fun handleEvents(addRoomViewEvent: AddRoomViewEvent?) {
        when (addRoomViewEvent) {
            AddRoomViewEvent.NavigateToHomeAndFinish -> {
                navigateToHome()
                finish()
            }

            else -> {}
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    lateinit var categoryAdapter: RoomCategoryAdapter

    private fun initViews() {
        categoryAdapter = RoomCategoryAdapter(viewModel.categories)
        viewBinding.lifecycleOwner = this
        viewBinding.vm = viewModel
        viewBinding.content.categoriesSpinner.adapter = categoryAdapter
        viewBinding.content.categoriesSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    itemView: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.onCategorySelected(position)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
    }
}