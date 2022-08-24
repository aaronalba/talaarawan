package com.aaron.talaarawan.data

import android.annotation.SuppressLint
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

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

    @ColumnInfo(name = "date")
    val entryDate: Long
)


@SuppressLint("SimpleDateFormat")
fun Entry.getFormattedDate(): String {
    return SimpleDateFormat("dd MMMM yyyy").format(Date(entryDate))
}

@SuppressLint("SimpleDateFormat")
fun Entry.getDayOfWeek(): String {
    return SimpleDateFormat("EEEE").format(Date(entryDate))
}