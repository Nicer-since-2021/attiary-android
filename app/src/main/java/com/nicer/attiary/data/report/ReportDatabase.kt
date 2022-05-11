package com.nicer.attiary.data.report

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.google.gson.Gson

@Database(entities = [Report::class], version = 1, exportSchema = false)
@TypeConverters( HashMapTypeConverter::class )
abstract class ReportDatabase: RoomDatabase() {
	abstract fun ReportDao(): ReportDao

	companion object {
		private var Instance: ReportDatabase? = null

		fun getInstance(context: Context, gson: Gson): ReportDatabase? {
			if (Instance == null) {
				synchronized(ReportDatabase::class) {
					Instance = Room.databaseBuilder(
						context,
						ReportDatabase::class.java,
						"report"
					).addTypeConverter(HashMapTypeConverter(gson)).build()
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