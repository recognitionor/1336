package com.ham.onettsix

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ham.onettsix.databinding.ActivityLotteryHistoryBinding

class LotteryHistoryActivity : AppCompatActivity(R.layout.activity_lottery_history) {

    private lateinit var binding: ActivityLotteryHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLotteryHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lotteryHistoryToolbarBack.setOnClickListener {
            finish()
        }
    }
}
