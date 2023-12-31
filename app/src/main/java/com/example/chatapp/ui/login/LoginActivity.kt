package com.example.chatapp.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.chatapp.R
import com.example.chatapp.databinding.ActivityLoginBinding
import com.example.chatapp.ui.home.HomeActivity
import com.example.chatapp.ui.register.RegisterActivity
import com.example.chatapp.ui.showMessage

class LoginActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.messageLiveData.observe(this) { message ->
            showMessage(
                message = message.message ?: "something went wrong",
                posActionName = "ok",
                posAction = message.onPositiveActionClick,
                nagActionName = message.negativeActionName,
                negAction = message.onNegativeActionClick,
                isCancelable = message.isCancelable
            )
        }
        viewModel.events.observe(this, ::handleEvents)
    }

    private fun handleEvents(loginViewEvent: LoginViewEvent?) {
        when (loginViewEvent) {
            LoginViewEvent.NavigateToHome -> {
                navToHome()
                finish()
            }

            LoginViewEvent.NavigateToRegister -> {
                startRegisterActivity()
                finish()
            }

            else -> {}
        }
    }

    private fun navToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun initViews() {
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewBinding.lifecycleOwner = this
        viewBinding.vm = viewModel

    }

    private fun startRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}