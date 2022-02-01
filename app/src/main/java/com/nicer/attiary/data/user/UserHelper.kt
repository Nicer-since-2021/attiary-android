package com.nicer.attiary.data.user

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UserHelper: RoomDatabase() {
	abstract fun userDao(): UserDao
}