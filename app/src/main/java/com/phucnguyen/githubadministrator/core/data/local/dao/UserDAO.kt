package com.phucnguyen.githubadministrator.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.phucnguyen.githubadministrator.core.data.local.model.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    suspend fun insertUsers(users: List<com.google.firebase.firestore.auth.User>)

    @Query("SELECT * FROM user WHERE userName = :userName")
    suspend fun getUserByName(userName: String): UserEntity

//    @Query("SELECT * FROM user")
//    suspend fun getAllUsers(): List<com.google.firebase.firestore.auth.User>
}