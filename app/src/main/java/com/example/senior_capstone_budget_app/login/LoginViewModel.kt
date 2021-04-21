package com.example.senior_capstone_budget_app.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.example.senior_capstone_budget_app.data.LoginRepository
import com.example.senior_capstone_budget_app.data.Result

import com.example.senior_capstone_budget_app.R

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String) : Int {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(username)

        return if (result is Result.Success) {
            _loginResult.value =
                LoginResult(success = LoggedInUserView(username = result.data.displayName))
            0
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
            1
        }
        return  2
    }

    fun loginDataChanged(username: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        }  else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return username.isNotBlank()

    }

}