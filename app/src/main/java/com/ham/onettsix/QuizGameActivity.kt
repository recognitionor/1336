package com.ham.onettsix

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.databinding.ActivityQuizBinding
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.QuizGameViewModel

class QuizGameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding

    private val activityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        }

    private val quizGameViewModel by lazy {
        ViewModelProviders.of(
            this, ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext)),
                PreferencesHelper.getInstance(applicationContext)
            )
        )[QuizGameViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupObserve() {
    }
}