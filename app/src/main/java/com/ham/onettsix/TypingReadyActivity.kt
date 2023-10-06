package com.ham.onettsix

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.ham.onettsix.adapter.TypingReadyPagerAdapter
import com.ham.onettsix.constant.ActivityResultKey
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.databinding.ActivityTypingReadyBinding
import com.ham.onettsix.fragment.EulaFragment
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.TypingReadyViewModel

class TypingReadyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTypingReadyBinding

    private var checkTerms: Boolean = false

    private var checkPrivacyPolicy: Boolean = false

    private var selectedSection: Int = 0

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
        setupUI()
        setupObserver()

    }

    private fun setupObserver() {


    }

    private fun setupUI() {
        val sectionsPagerAdapter = TypingReadyPagerAdapter(
            this,
            supportFragmentManager,
            checkTerms, checkPrivacyPolicy)
        val viewPager: ViewPager = binding.vpTypingReady
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabTypingReady
        tabs.setupWithViewPager(viewPager)
        tabs.selectTab(tabs.getTabAt(selectedSection))
    }
}