package com.ham.onettsix

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.databinding.ActivityTypingRegisterBinding
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.TypingGameViewModel

class TypingRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTypingRegisterBinding

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
        binding = ActivityTypingRegisterBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setupObserver()
        binding.typingGameRegisterToolbarBack.setOnClickListener {
            finish()
        }
    }

    private fun setupObserver() {

    }
}