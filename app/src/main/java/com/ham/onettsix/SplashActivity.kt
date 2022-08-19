package com.ham.onettsix

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.SplashViewModel
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity(R.layout.activity_splash) {

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
        Log.d("jhlee", "onCreate")
        setupObserver()
        splashViewModel.refreshLogin()

        splash_error_btn.setOnClickListener {
            splashViewModel.refreshLogin()
        }
    }

    private fun setupObserver() {
        splashViewModel.result.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    finish()
                    activityResult.launch(Intent(this, MainActivity::class.java))
                }
                Status.LOADING -> {
                    splash_progress.visibility = View.VISIBLE
                    splash_error_btn.visibility = View.GONE
                }
                Status.ERROR -> {
                    splash_progress.visibility = View.GONE
                    splash_error_btn.visibility = View.VISIBLE
                }
            }
        }
    }
}