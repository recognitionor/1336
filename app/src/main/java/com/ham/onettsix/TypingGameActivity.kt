package com.ham.onettsix

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.databinding.ActivityTypingGameBinding
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.TypingGameViewModel

class TypingGameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTypingGameBinding

    private val typingGameViewModel by lazy {
        ViewModelProviders.of(
            this, ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext)),
                PreferencesHelper.getInstance(applicationContext)
            )
        )[TypingGameViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTypingGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupObserver()
        typingGameViewModel.startGame()

    }

    private fun setupObserver() {
        typingGameViewModel.typingGame.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.typingGameProgressBar.visibility = View.GONE
                    it.data?.let { typingGame ->
                        binding.typingGameQuestionTv.text = typingGame.data.content
                    }
                }

                Status.ERROR -> {
                    binding.typingGameProgressBar.visibility = View.GONE
                }

                Status.LOADING -> {
                    binding.typingGameProgressBar.visibility = View.VISIBLE
                }
            }
        }

        typingGameViewModel.typingGameTimer.observe(this) {
            binding.typingGameTimerTv.text = String.format("%.3f", it)
        }
    }
}