package com.nicer.attiary.data.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class User {

	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = "user_id")
	var userId: Long? = null

	@ColumnInfo
	var email: String = ""

	@ColumnInfo
	var name: String = ""

	@ColumnInfo(name = "birthday_month")
	var birthdayMonth: Int = 0

	@ColumnInfo(name = "birthday_day")
	var birthdayDay: Int = 0

	constructor()

	class Builder(
		val name: String, val email: String) {

		var birthdayMonth = 0
		var birthdayDay = 0

		fun birthdayMonth(birthdayMonth: Int): Builder {
			this.birthdayMonth = birthdayMonth
			return this
		}

		fun birthdayDay(birthdayDay: Int): Builder {
			this.birthdayDay = birthdayDay
			return this
		}

		fun build(): User {
			return User(this)
		}
	}

	constructor(builder: Builder) {
		name = builder.name
		email = builder.email
		birthdayMonth = builder.birthdayMonth
		birthdayDay = builder.birthdayDay
	}
}
