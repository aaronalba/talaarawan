package com.aaron.talaarawan.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EntryDao {
    @Query("SELECT * FROM entry")
    fun getEntries(): Flow<List<Entry>>

    @Query("SELECT * FROM entry WHERE id = :id")
    fun getEntry(id: Int): Flow<Entry>

    @Insert
    suspend fun insert(entry: Entry): Long

    @Update
    suspend fun update(entry: Entry)

    @Delete
    suspend fun delete(entry: Entry)
}