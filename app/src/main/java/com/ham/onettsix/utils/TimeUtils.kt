package com.ham.onettsix.utils

import android.content.Context
import com.ham.onettsix.R
import java.math.BigDecimal

class TimeUtils {
    enum class TimeValue(val value: Int, val maximum: Int, val msg: String) {
        SEC(60, 60, "분 전"), MIN(60, 24, "시간 전"), HOUR(24, 30, "일 전"), DAY(30, 12, "달 전"), MONTH(
            12, Int.MAX_VALUE, "년 전"
        )
    }

    companion object {

        fun <T> getSecondString(sec: T): String {
            val result = BigDecimal(sec.toString()).multiply(BigDecimal("0.001"))
            return result.toString()
        }

        fun timeDiff(ctx: Context, time: Long): String {
            val curTime = System.currentTimeMillis()
            val diffTime = curTime - (time)

            val seconds = diffTime / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24
            val months = days / 30
            val years = days / 365

            return when {
                years > 0 -> ctx.getString(R.string.year_ago, years)
                months > 0 -> ctx.getString(R.string.month_ago, months)
                days > 0 -> ctx.getString(R.string.day_ago, days)
                hours > 0 -> ctx.getString(R.string.hour_ago, hours)
                minutes > 0 -> ctx.getString(R.string.min_ago, minutes)
                else -> ctx.getString(R.string.sec_ago)
            }
        }

    }
}