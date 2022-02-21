package com.nicer.attiary.data.report

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface ReportDao {
	@Insert(onConflict = REPLACE)
	fun insert(report: Report)

	@Query("select * from report where date = :rDate")
	fun findByDate(rDate: Long): Report

	@Query("DELETE FROM report where date = :rDate")
	fun delete(rDate: Long)

}