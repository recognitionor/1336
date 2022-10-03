package com.ham.onettsix.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import com.ham.onettsix.R
import kotlinx.android.synthetic.main.view_remain_drawing_time.view.*
import kotlinx.coroutines.*
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*


class RemainTimeView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private var days: Int = 0

    private var hours: Int = 0

    private var mins: Int = 0

    private var secs: Int = 0

    private var coroutineScope: CoroutineScope? = null


    init {
        val view = inflate(context, R.layout.view_remain_drawing_time, null)
        view.apply {}
        addView(view)
    }

    fun setStartTime(limitedDate: Long = Long.MAX_VALUE) {
        CoroutineScope(Dispatchers.Default).launch {
            this@RemainTimeView.coroutineScope = this
            if (limitedDate <= System.currentTimeMillis()) {
                val defaultTime = resources.getText(R.string.home_remain_drawing_time_default)
                remain_time_days_tv.post {
                    remain_time_days_tv.text = defaultTime
                }
                remain_time_hour_tv.post {
                    remain_time_hour_tv.text = defaultTime
                }
                remain_time_mins_tv.post {
                    remain_time_mins_tv.text = defaultTime
                }
                remain_time_secs_tv.post {
                    remain_time_secs_tv.text = defaultTime
                }
            } else {
                while (true) {
                    val targetTime =
                        LocalDateTime.ofInstant(Date(limitedDate).toInstant(), ZoneId.systemDefault())
                    val totalSec = LocalDateTime.now().until(targetTime, ChronoUnit.SECONDS)
                    val day = totalSec / (60 * 60 * 24)
                    val hour = (totalSec % (60 * 60 * 24)) / (60 * 60)
                    val min = (totalSec % (60 * 60)) / (60)
                    val sec = totalSec % (60)
                    remain_time_days_tv.post {
                        remain_time_days_tv.text = day.toString()
                    }
                    remain_time_hour_tv.post {
                        remain_time_hour_tv.text = hour.toString()
                    }
                    remain_time_mins_tv.post {
                        remain_time_mins_tv.text = min.toString()
                    }

                    remain_time_secs_tv.post {
                        remain_time_secs_tv.text = sec.toString()
                    }
                    delay(1000)
                }
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        coroutineScope?.cancel()
    }
}