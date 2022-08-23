package com.aaron.talaarawan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aaron.talaarawan.data.User
import com.aaron.talaarawan.data.UserDao
import kotlinx.coroutines.launch

class RegisterViewModel(private val userDao: UserDao) : ViewModel() {

    /**
     * Saves the given user to the database
     */
    fun registerUser(fullName: String, pin: String) {
        val user = User(
            username = "",
            userFullName = fullName,
            userPin = pin
        )
        insertUser(user)
    }

    /**
     *
     */
    fun isUserValid(fullName: String): Boolean {
        return fullName.isNotBlank()
    }

    /**
     * Returns true if the pin is not empty and matches the re-entered pin
     */
    fun isPinValid(pin: String, pin2: String): Boolean {
        if (pin.isNotBlank() || pin2.isNotBlank()) {
            if (pin == pin2) {
                return true
            }
        }
        return false
    }

    /**
     * Interacts with [UserDao] to insert a [User] to the database
     */
    private fun insertUser(user: User) {
        viewModelScope.launch {
            userDao.insert(user)
        }
    }
}

class RegisterViewModelFactory(private val userDao: UserDao)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}