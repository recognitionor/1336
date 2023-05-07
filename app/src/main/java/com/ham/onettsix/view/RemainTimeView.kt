package com.ham.onettsix.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.ham.onettsix.R
import com.ham.onettsix.databinding.ToolbarLayoutBinding
import com.ham.onettsix.databinding.ViewRemainDrawingTimeBinding
import kotlinx.coroutines.*
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*


class RemainTimeView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private lateinit var binding: ViewRemainDrawingTimeBinding

    private var days: Int = 0

    private var hours: Int = 0

    private var mins: Int = 0

    private var secs: Int = 0

    private var coroutineScope: CoroutineScope? = null


    init {
        val view = inflate(context, R.layout.view_remain_drawing_time, null)
        binding = ViewRemainDrawingTimeBinding.inflate(LayoutInflater.from(context))
        view.apply {}
        addView(binding.root)
    }

    fun stopTime() {
        this@RemainTimeView.coroutineScope?.cancel()
        val defaultTime = resources.getString(R.string.home_remain_drawing_time_default)
        binding.remainTimeHourTv.post {
            binding.remainTimeHourTv.text = defaultTime
        }
        binding.remainTimeMinsTv.post {
            binding.remainTimeHourTv.text = defaultTime
        }
        binding.remainTimeSecsTv.post {
            binding.remainTimeHourTv.text = defaultTime
        }
    }

    fun setStartTime(limitedDate: Long = Long.MAX_VALUE) {
        coroutineScope = CoroutineScope(Dispatchers.Default)
        coroutineScope?.launch {
            if (limitedDate <= System.currentTimeMillis()) {
                val defaultTime = resources.getText(R.string.home_remain_drawing_time_default)
                binding.remainTimeDaysTv.post {
                    binding.remainTimeDaysTv.text = defaultTime
                }
                binding.remainTimeHourTv.post {
                    binding.remainTimeHourTv.text = defaultTime
                }
                binding.remainTimeMinsTv.post {
                    binding.remainTimeMinsTv.text = defaultTime
                }
                binding.remainTimeSecsTv.post {
                    binding.remainTimeSecsTv.text = defaultTime
                }
            } else {
                while (true) {
                    Log.d("jhlee", "while")
                    val targetTime =
                        LocalDateTime.ofInstant(
                            Date(limitedDate).toInstant(),
                            ZoneId.systemDefault()
                        )
                    val totalSec = LocalDateTime.now().until(targetTime, ChronoUnit.SECONDS)
                    val day = totalSec / (60 * 60 * 24)
                    val hour = (totalSec % (60 * 60 * 24)) / (60 * 60)
                    val min = (totalSec % (60 * 60)) / (60)
                    val sec = totalSec % (60)

                    binding.remainTimeDaysTv.post {
                        binding.remainTimeDaysTv.text = day.toString()
                    }
                    binding.remainTimeHourTv.post {
                        binding.remainTimeHourTv.text = hour.toString()
                        if (coroutineScope?.isActive == true) {

                        }
                    }
                    binding.remainTimeMinsTv.post {
                        binding.remainTimeMinsTv.text = min.toString()
                        if (coroutineScope?.isActive == true) {
                            binding.remainTimeMinsTv.text = min.toString()
                        }
                    }
                    binding.remainTimeSecsTv.post {
                        binding.remainTimeSecsTv.text = sec.toString()
                        if (coroutineScope?.isActive == true) {
                            binding.remainTimeSecsTv.text = sec.toString()
                        }
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