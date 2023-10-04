package com.ham.onettsix

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.databinding.ActivityTypingReadyBinding
import com.ham.onettsix.dialog.OneButtonDialog
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.TypingReadyViewModel

class TypingReadyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTypingReadyBinding

    private val typingReadyViewModel by lazy {
        ViewModelProviders.of(
            this, ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext)),
                PreferencesHelper.getInstance(applicationContext)
            )
        )[TypingReadyViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTypingReadyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupObserver()
        binding.getBtn.setOnClickListener {
            val dialog = OneButtonDialog("타이핑을 시작합니다.", getString(R.string.list_empty)) { dialog ->
                dialog.dismiss()
                finish()
                startActivity(Intent(this, TypingGameActivity::class.java))
            }
            dialog.dialog?.setCanceledOnTouchOutside(false)
            dialog.show(supportFragmentManager, OneButtonDialog.TAG)
        }
    }

    private fun setupObserver() {


    }
}