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

	// where date like "202205%" -> 202205로 시작하는 레코드
	@Query("SELECT * FROM report WHERE date like :yearMonth ORDER BY happiness DESC LIMIT 3")
	fun findHappinessTop3(yearMonth: String): MutableList<Report>

	@Query("SELECT * FROM report where date like :yearMonth ORDER BY date")
	fun findByYearAndMonth(yearMonth: String): MutableList<Report>

	@Query ("update report set representative = :emotion where date = :rDate" )
	fun updateRepresentative(emotion: String, rDate: String)
}