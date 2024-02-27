package com.ham.onettsix.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.ham.onettsix.adapter.TypingReadyPagerAdapter
import com.ham.onettsix.databinding.FragmentTypingReadyBinding
import com.ham.onettsix.dialog.AdDialog

class TypingFragment : Fragment() {

    private lateinit var binding: FragmentTypingReadyBinding

    private var checkTerms: Boolean = false

    private var checkPrivacyPolicy: Boolean = false

    private var selectedSection: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTypingReadyBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        val sectionsPagerAdapter = TypingReadyPagerAdapter(
            requireContext(), childFragmentManager, checkTerms, checkPrivacyPolicy
        )
        val viewPager: ViewPager = binding.vpTypingReady
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabTypingReady
        tabs.setupWithViewPager(viewPager)
        tabs.selectTab(tabs.getTabAt(selectedSection))

    }
}
