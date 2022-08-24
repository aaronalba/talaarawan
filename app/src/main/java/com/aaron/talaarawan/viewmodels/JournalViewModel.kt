package com.aaron.talaarawan.viewmodels

import androidx.lifecycle.*
import com.aaron.talaarawan.data.Entry
import com.aaron.talaarawan.data.EntryDao
import com.aaron.talaarawan.data.UserDao
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

    // ---------------------------
    // edit mode property
    private val _isEditing = MutableLiveData(true)
    val isEditing: LiveData<Boolean> = _isEditing

    /**
     * Updates the value of the isEditing property.
     */
    fun setEditing(value: Boolean) {
        _isEditing.value = value
    }

    // ---------------------------
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

    // ----- instance creation functions -------
    /**
     * Creates a new entry instance and assigns it as the current selected entry.
     */
    fun createNewEntry() {
        val newEntry = Entry(entryTitle = "", entryBody = "", entryDate = Date().time)
        _entry.value = newEntry
    }

    // ----- database functions ------
    /**
     * Function for saving the newly edited entry to the database
     */
    fun saveEntry(title: String, body: String) {
        // update the title and body values of the selected entry
        updateSelectedEntry(title, body)

        // determine if the entry is not yet in the database
        entryList.value?.let { list ->
            list.forEach { e ->
                if (e.id == entry.value?.id) {
                    // use database update if the entry is not yet saved prior
                    updateEntry()
                    return
                }
            }
        }
        // use database insert if it is a newly created entry
        insertEntry()
    }

    /**
     * Interacts with the [EntryDao] to insert the selected entry into the database
     */
    private fun insertEntry() {
        viewModelScope.launch {
            entryDao.insert(
                _entry.value
                    ?: throw IllegalStateException("Cannot insert to database. Selected entry is null")
            )
        }
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