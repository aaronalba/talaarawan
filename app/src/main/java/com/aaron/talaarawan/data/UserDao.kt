package com.aaron.talaarawan.data

import androidx.room.*

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUser(id: Int): User

    @Insert
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)
}