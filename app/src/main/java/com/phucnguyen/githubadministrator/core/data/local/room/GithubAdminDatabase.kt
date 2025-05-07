package com.phucnguyen.githubadministrator.core.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.phucnguyen.githubadministrator.core.data.local.dao.UserDao
import com.phucnguyen.githubadministrator.core.data.local.model.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class GithubAdminDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}