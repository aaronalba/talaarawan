package com.aaron.talaarawan.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class to represent a journal entry in the database.
 */

@Entity(tableName = "entry")
data class Entry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "title")
    val entryTitle: String,

    @ColumnInfo(name = "body")
    val entryBody: String,
)