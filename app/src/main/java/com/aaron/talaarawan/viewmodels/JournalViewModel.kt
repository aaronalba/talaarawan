package com.aaron.talaarawan.viewmodels

import androidx.lifecycle.*
import com.aaron.talaarawan.data.Entry
import com.aaron.talaarawan.data.EntryDao
import com.aaron.talaarawan.data.UserDao
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.util.*
import kotlin.IllegalStateException

/**
 * The shared ViewModel between [EntryListFragment] and [DetailFragment].
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
     * Update the selected entry property with another entry.
     */
    fun updateSelectedEntry(entry: Entry) {
        _entry.value = entry
    }

    /**
     * Update the selected entry property with a new title and body values.
     */
    private fun updateSelectedEntry(title: String, body: String) {
        entry.value?.let {
            val newEntry = it.copy(
                entryTitle = title,
                entryBody = body
            )
            _entry.value = newEntry
        }
    }

    /**
     * Creates a new entry instance and assigns it as the current selected entry.
     */
    fun createNewEntry() {
        val newEntry = Entry(entryTitle = "", entryBody = "", entryDate = Date().time)
        _entry.value = newEntry
    }

    /**
     * Function for saving the newly edited entry to the database
     */
    fun saveEntry(title: String, body: String) {
        // update the title and body values of the selected entry
        updateSelectedEntry(title, body)

        viewModelScope.launch {
            entry.value?.let {
                // determine if the entry is not yet in the database
                if (it.id != 0) {
                    // existing entry, update the database
                    updateEntry()

                } else {
                    // new entry, insert into the database
                    val newId = insertEntry()

                    // update the id of the selected entry
                    val newEntry = _entry.value!!.copy(id = newId.toInt())
                    _entry.value = newEntry
                }
            }
        }
    }

    /**
     * Interacts with the [EntryDao] to insert the selected entry into the database
     */
    private suspend fun insertEntry(): Long {
        return entryDao.insert(
            _entry.value
                ?: throw IllegalStateException("Cannot insert to database. Selected entry is null")
        )
    }

    /**
     * Interacts with the [EntryDao] to update the selected entry into the database
     */
    private fun updateEntry() {
        viewModelScope.launch {
            entryDao.update(
                _entry.value
                    ?: throw IllegalStateException("Cannot insert to database. Selected entry is null")
            )
        }
    }

    /**
     * Interacts with [EntryDao] to delete the selected entry from the database
     */
    suspend fun deleteEntry() {
        viewModelScope.launch {
            entryDao.delete(
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