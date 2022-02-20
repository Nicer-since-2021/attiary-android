package com.nicer.attiary.data.app

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import java.util.*

@Dao
interface ReportDao {
	@Insert(onConflict = REPLACE)
	fun insert(report: Report)

	@Query("select * from report where date = :rDate")
	fun findByDate(rDate: Long): Report

	@Query("DELETE FROM report where date = :rDate")
	fun delete(rDate: Long)

}