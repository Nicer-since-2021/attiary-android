package com.nicer.attiary.data.user

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UserHelper: RoomDatabase() {
	abstract fun userDao(): UserDao

	companion object {
		private var Instance: UserHelper? = null

		fun getInstance(context: Context): UserHelper? {
			if (Instance == null) {
				synchronized(UserHelper::class) {
					Instance = Room.databaseBuilder(
						context,
						UserHelper::class.java,
						"user"
					).build()
					// migration // .addMigrations(MIGRATION_1_2)
				}
			}
			return Instance
		}

		fun deleteInstance() {
			Instance = null
		}
	}
}