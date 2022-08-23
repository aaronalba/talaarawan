package com.aaron.talaarawan

import androidx.lifecycle.*
import com.aaron.talaarawan.data.EntryDao
import com.aaron.talaarawan.data.User
import com.aaron.talaarawan.data.UserDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * The ViewModel for the application
 */
class JournalViewModel(
    private val userDao: UserDao,
    private val entryDao: EntryDao
) : ViewModel() {

    /**
     * Function to save a user to the database
     */
    fun addUser(username: String = "",fullName: String, pin: String) {
        val newUser = createUser(username,fullName,pin)
        insertUser(newUser)
    }

    /**
     * Interacts with the [UserDao] to insert a user to the database
     */
    private fun insertUser(user: User) {
        viewModelScope.launch {
            userDao.insert(user)
        }
    }

    /**
     * Creates an instance of a [User]
     */
    private fun createUser(
        username: String,
        fullName: String,
        pin: String
    ): User {
        return User(
            username = username,
            userFullName = fullName,
            userPin = pin
        )
    }
}


/**
 * Used to create JournalViewModel instance.
 */
class JournalViewModelFactory(
    private val userDao: UserDao,
    private val entryDao: EntryDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JournalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JournalViewModel(userDao, entryDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}