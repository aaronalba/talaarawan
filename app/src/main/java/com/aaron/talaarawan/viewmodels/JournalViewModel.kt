package com.aaron.talaarawan.viewmodels

import androidx.lifecycle.*
import com.aaron.talaarawan.data.EntryDao
import com.aaron.talaarawan.data.User
import com.aaron.talaarawan.data.UserDao
import kotlinx.coroutines.launch

/**
 * The ViewModel for the application
 */
class JournalViewModel(
    private val userDao: UserDao,
    private val entryDao: EntryDao
) : ViewModel() {

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