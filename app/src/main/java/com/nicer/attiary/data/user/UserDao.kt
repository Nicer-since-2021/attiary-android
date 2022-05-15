package com.nicer.attiary.data.user

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import java.util.*

@Dao
interface UserDao {
	@Insert(onConflict = REPLACE)
	fun insert(user: User)

	@Query("select * from user where user_id = :userId")
	fun findByUserId(userId: Long): User

	@Delete
	fun delete(user: User)

	@Query("select email from User")
	fun getEmail(): List<String>

	@Query("select name from User")
	fun getName(): List<String>

	@Query("select birthday_day from User")
	fun getBdayDay(): List<Int>

	@Query("select birthday_month from User")
	fun getBdayMonth(): List<Int>

	@Query ("update user set name = :newName where user_id = 1" )
	fun updateName(newName: String)

	@Query ("update user set birthday_day = :newDay where user_id = 1" )
	fun updateBdayDay(newDay: Int)

	@Query ("update user set birthday_month = :newMonth where user_id = 1" )
	fun updateBdayMonth(newMonth: Int)
}