package com.aaron.talaarawan.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getUsers(): Flow<List<User>>

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUser(id: Int): Flow<User>

    @Insert
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)
}