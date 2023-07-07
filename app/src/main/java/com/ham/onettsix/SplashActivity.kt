package com.ham.onettsix

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import android.net.Uri

class SplashActivity : AppCompatActivity() {

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
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
        }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 200)
        createNotificationChannel(this)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupObserver()
        splashViewModel.refreshLogin()
        splashViewModel.getRewardUnit()
        binding.splashErrorBtn.setOnClickListener {
            splashViewModel.refreshLogin()
        }
        // ATTENTION: This was auto-generated to handle app links.
        val appLinkIntent: Intent = intent
        val appLinkAction: String? = appLinkIntent.action
        val appLinkData: Uri? = appLinkIntent.data
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "my-notification-channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channelId = "${context.packageName}-$name"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = "my notification channel description"
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
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