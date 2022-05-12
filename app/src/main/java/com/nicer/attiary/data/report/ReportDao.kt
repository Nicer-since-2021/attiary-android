package com.nicer.attiary.data.report

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface ReportDao {
	@Insert(onConflict = REPLACE)
	fun insert(report: Report)

	@Query("select * from report where date = :rDate ORDER BY report_id DESC")
	fun findByDate(rDate: String): Report

	@Query("delete from report where date = :rDate")
	fun delete(rDate: String)

	@Query("SELECT * FROM report ORDER BY date DESC LIMIT :rowCount")
	fun findTop10(rowCount: Int) : MutableList<Report>

	@Query("SELECT * FROM report ORDER BY date DESC, happiness DESC LIMIT 3")
	fun findHappinessTop3(): MutableList<Report>
}