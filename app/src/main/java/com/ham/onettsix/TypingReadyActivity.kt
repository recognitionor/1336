package com.ham.onettsix

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.ham.onettsix.adapter.TypingReadyPagerAdapter
import com.ham.onettsix.databinding.ActivityTypingReadyBinding

class TypingReadyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTypingReadyBinding

    private var checkTerms: Boolean = false

    private var checkPrivacyPolicy: Boolean = false

    private var selectedSection: Int = 0



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
            this, supportFragmentManager, checkTerms, checkPrivacyPolicy
        )
        val viewPager: ViewPager = binding.vpTypingReady
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabTypingReady
        tabs.setupWithViewPager(viewPager)
        tabs.selectTab(tabs.getTabAt(selectedSection))

        binding.typingGameToolbarBack.getBinding().addBtn.setOnClickListener {
            startActivity(Intent(this, TypingRegisterActivity::class.java))
        }
        binding.typingGameToolbarBack.getBinding().backBtn.setOnClickListener {
            finish()
        }
    }
}