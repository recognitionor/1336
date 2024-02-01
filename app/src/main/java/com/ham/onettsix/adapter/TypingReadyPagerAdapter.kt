package com.ham.onettsix.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ham.onettsix.R
import com.ham.onettsix.fragment.TypingNormalFragment
import com.ham.onettsix.fragment.TypingRankFragment


class TypingReadyPagerAdapter(
    private val context: Context,
    fm: FragmentManager,
    private val checkTerms: Boolean,
    private val checkPrivacyPolicy: Boolean
) : FragmentPagerAdapter(fm) {


    private val tiles = arrayOf(
        R.string.typing_tab_rank_game, R.string.typing_tab_speed_game
    )

    override fun getItem(position: Int): Fragment {
        return if (position == 0) {
            TypingRankFragment.newInstance()
        } else {
            TypingNormalFragment.newInstance()
        }

    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(tiles[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 2
    }
}