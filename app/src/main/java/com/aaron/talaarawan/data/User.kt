package com.aaron.talaarawan.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class to represent a User in the database.
 * Note: Currently, the app only supports a single user.
 */

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "full_name")
    val userFullName: String,

    @ColumnInfo(name = "pin")
    val userPin: String
)