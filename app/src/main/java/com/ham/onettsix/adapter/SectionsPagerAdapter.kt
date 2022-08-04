package com.ham.onettsix.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ham.onettsix.R
import com.ham.onettsix.fragment.EulaFragment

private val TAB_TITLES = arrayOf(
    R.string.eula_item_terms,
    R.string.eula_item_privacy_policy
)

class SectionsPagerAdapter(
    private val context: Context,
    fm: FragmentManager,
    private val checkTerms: Boolean,
    private val checkPrivacyPolicy: Boolean,
    private val listener: EulaFragment.OnEulaReadListener
) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return EulaFragment.newInstance(position, listener)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 2
    }
}