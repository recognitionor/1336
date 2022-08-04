package com.ham.onettsix

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.ham.onettsix.adapter.SectionsPagerAdapter
import com.ham.onettsix.constant.ActivityResultKey
import com.ham.onettsix.constant.ExtraKey
import com.ham.onettsix.fragment.EulaFragment
import kotlinx.android.synthetic.main.activity_eula.*

class EulaActivity : AppCompatActivity(R.layout.activity_eula) {

    private var checkTerms: Boolean = false

    private var checkPrivacyPolicy: Boolean = false

    private var selectedSection: Int = 0

    companion object {
        const val RESULT_KEY_CHECK_TERMS = "RESULT_KEY_CHECK_TERMS"
        const val RESULT_KEY_PRIVACY_POLICY = "RESULT_KEY_PRIVACY_POLICY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedSection = intent.getIntExtra(ExtraKey.PERMISSION_CLICK_SECTION, 0)
        checkTerms = intent.getBooleanExtra(ExtraKey.PERMISSION_CHECKED_TERMS, false)
        checkPrivacyPolicy = intent.getBooleanExtra(ExtraKey.PERMISSION_PRIVACY_POLICY, false)
        setupUI()
    }

    private fun setupUI() {
        val sectionsPagerAdapter = SectionsPagerAdapter(
            this,
            supportFragmentManager,
            checkTerms, checkPrivacyPolicy,
            object : EulaFragment.OnEulaReadListener {
                override fun onFinishRead(sectionNumber: Int) {
                    when (sectionNumber) {
                        EulaFragment.SECTION_NUMBER_0 -> {
                            checkTerms = true
                        }
                        EulaFragment.SECTION_NUMBER_1 -> {
                            checkPrivacyPolicy = true
                        }
                    }
                    intent.putExtra(RESULT_KEY_CHECK_TERMS, checkTerms)
                    intent.putExtra(RESULT_KEY_PRIVACY_POLICY, checkPrivacyPolicy)
                    setResult(ActivityResultKey.EULA_RESULT, intent)
                }

                override fun getCheckState(sectionNumber: Int): Boolean {
                    when (sectionNumber) {
                        EulaFragment.SECTION_NUMBER_0 -> {
                            return checkTerms
                        }
                        EulaFragment.SECTION_NUMBER_1 -> {
                            return checkPrivacyPolicy
                        }
                    }
                    return false
                }
            })

        val viewPager: ViewPager = vp_eula
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = tab_eula
        tabs.setupWithViewPager(viewPager)
        tabs.selectTab(tabs.getTabAt(selectedSection))
    }

    fun onClick(view: View) = finish()

}