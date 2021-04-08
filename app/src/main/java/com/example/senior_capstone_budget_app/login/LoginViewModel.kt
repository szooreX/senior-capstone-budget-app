package com.example.senior_capstone_budget_app.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.example.senior_capstone_budget_app.data.LoginRepository
import com.example.senior_capstone_budget_app.data.Result

import com.example.senior_capstone_budget_app.R

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) : Int {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(username, password)

        return if (result is Result.Success) {
            _loginResult.value =
                LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
            0
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
            1
        }
        return  2
    }

}