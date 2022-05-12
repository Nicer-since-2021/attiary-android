package com.nicer.attiary.util


class RDate {
    companion object {
        fun toRDate(y : Int, m : Int, d : Int) : String {
            return "$y" + padding(m + 1) + padding(d)
        }

        private fun padding(date : Int): String {
            if (date < 10)
                return "0$date"
            return "$date"
        }
    }

}