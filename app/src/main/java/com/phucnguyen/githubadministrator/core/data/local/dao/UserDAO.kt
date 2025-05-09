package com.phucnguyen.githubadministrator.core.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.phucnguyen.githubadministrator.core.data.local.model.UserEntity

@Dao
interface UserDao {
    @Update
    suspend fun updateUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Query("SELECT * FROM user WHERE userName = :userName")
    suspend fun getUserByName(userName: String): UserEntity

    @Query("SELECT * FROM user")
    fun getAllUsers(): PagingSource<Int, UserEntity>

    @Query("DELETE FROM user")
    suspend fun deleteAllUsers()
}