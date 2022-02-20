package com.nicer.attiary.data.app

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Report::class], version = 1, exportSchema = false)
abstract class ReportDatabase: RoomDatabase() {
	abstract fun ReportDao(): ReportDao

	companion object {
		private var Instance: ReportDatabase? = null

		fun getInstance(context: Context): ReportDatabase? {
			if (Instance == null) {
				synchronized(ReportDatabase::class) {
					Instance = Room.databaseBuilder(
						context,
						ReportDatabase::class.java,
						"report"
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