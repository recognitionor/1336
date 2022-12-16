package com.ham.onettsix

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.databinding.ActivitySignUpBinding
import com.ham.onettsix.databinding.ActivitySplashBinding
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.SplashViewModel

class SplashActivity : AppCompatActivity(R.layout.activity_splash) {

    private lateinit var binding: ActivitySplashBinding

    private val splashViewModel by lazy {
        ViewModelProviders.of(
            this,
            ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(this.applicationContext)),
                PreferencesHelper.getInstance(this)
            )
        )[SplashViewModel::class.java]
    }

    private val activityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setupObserver()
        splashViewModel.refreshLogin()
        binding.splashErrorBtn.setOnClickListener {
            splashViewModel.refreshLogin()
        }
    }

    private fun setupObserver() {
        splashViewModel.refreshResult.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    finish()
                    activityResult.launch(Intent(this, MainActivity::class.java))
                }
                Status.LOADING -> {

                    binding.splashProgress.visibility = View.VISIBLE
                    binding.splashErrorBtn.visibility = View.GONE
                }
                Status.ERROR -> {
                    binding.splashProgress.visibility = View.GONE
                    binding.splashErrorBtn.visibility = View.VISIBLE
                }
            }
        }
    }
}