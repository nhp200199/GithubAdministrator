package com.phucnguyen.githubadministrator.core.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.phucnguyen.githubadministrator.core.data.local.dao.UserDao
import com.phucnguyen.githubadministrator.core.data.local.model.UserEntity

const val DB_NAME = "github_admin_database"
const val DB_VERSION = 1

@Database(entities = [UserEntity::class], version = DB_VERSION, exportSchema = false)
abstract class GithubAdminDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}