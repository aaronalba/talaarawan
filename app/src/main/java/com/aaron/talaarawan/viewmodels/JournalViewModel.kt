package com.aaron.talaarawan.viewmodels

import androidx.lifecycle.*
import com.aaron.talaarawan.data.Entry
import com.aaron.talaarawan.data.EntryDao
import com.aaron.talaarawan.data.UserDao
import kotlinx.coroutines.launch
import java.util.*
import kotlin.IllegalStateException

/**
 * The ViewModel for the application
 */
class JournalViewModel(
    private val userDao: UserDao,
    private val entryDao: EntryDao
) : ViewModel() {

    // list of entries property
    val entryList: LiveData<List<Entry>> = entryDao.getEntries().asLiveData()

    // edit mode property
    private val _isEditing = MutableLiveData(true)
    val isEditing: LiveData<Boolean> = _isEditing

    /**
     * Updates the value of the isEditing property.
     */
    fun setEditing(value: Boolean) {
        _isEditing.value = value
    }

    // the selected entry property
    private val _entry = MutableLiveData<Entry>()
    val entry: LiveData<Entry> = _entry

    /**
     * Creates a new entry instance and assigns it as the current selected entry.
     */
    fun createNewEntry() {
        val newEntry = Entry(entryTitle = "", entryBody = "", entryDate = Date().time)
        _entry.value = newEntry
    }

    /**
     * Updates the title of the selected entry.
     */
    fun updateEntryTitle(title: String) {
        val newEntry: Entry = _entry.value?.copy(entryTitle = title)
            ?: throw IllegalStateException("Selected entry is null")
        _entry.value = newEntry
    }

    /**
     * Updates the body of the selected entry.
     */
    fun updateEntryBody(body: String) {
        val newEntry: Entry = _entry.value?.copy(entryBody = body)
            ?: throw IllegalStateException("Selected entry is null")
        _entry.value = newEntry
    }

    /**
     * Interacts with the [EntryDao] to insert the selected entry into the database
     */
    fun insertEntry() {
        viewModelScope.launch {
            entryDao.insert(
                _entry.value
                    ?: throw IllegalStateException("Cannot insert to database. Selected entry is null")
            )
        }
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