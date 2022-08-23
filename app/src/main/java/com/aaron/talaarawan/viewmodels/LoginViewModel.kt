package com.aaron.talaarawan.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.aaron.talaarawan.data.User
import com.aaron.talaarawan.data.UserDao

class LoginViewModel(private val userDao: UserDao) : ViewModel() {

    // list of all users property from the database
    val allUsers: LiveData<List<User>> = userDao.getUsers().asLiveData()

    /**
     * Returns true if the entered pin is not blank
     */
    fun isPinValid(pin: String): Boolean {
        return pin.isNotBlank()
    }
}

class LoginViewModelFactory(private val userDao: UserDao)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}