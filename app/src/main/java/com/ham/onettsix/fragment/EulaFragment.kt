package com.ham.onettsix.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ham.onettsix.R
import com.ham.onettsix.data.api.UrlInfo
import com.ham.onettsix.databinding.FragmentEulaBinding
import kotlin.math.abs


class EulaFragment(private val sectionNumber: Int, private val listener: OnEulaReadListener) :
    Fragment() {

    private var isReadDone = false

    private lateinit var binding: FragmentEulaBinding

    interface OnEulaReadListener {
        fun onFinishRead(sectionNumber: Int)
        fun getCheckState(sectionNumber: Int): Boolean
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentEulaBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url = if (sectionNumber == 0) "https://eula.onettsix.com/get/eula" else "https://eula.onettsix.com/get/personalPrivacy"

        binding.webviewEula.loadUrl(url)
        isReadDone = listener.getCheckState(sectionNumber)
        binding.webviewEula.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            val standardValue = binding.webviewEula.contentHeight * binding.webviewEula.scale
            val offset: Float = standardValue / 10

            val currentScroll = binding.webviewEula.height + scrollY
            if (abs(currentScroll - standardValue) <= offset) {
                if (!isReadDone) {
                    isReadDone = true
                    listener.onFinishRead(sectionNumber)
                    Toast.makeText(
                        this@EulaFragment.context, getString(R.string.confirm), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        const val SECTION_NUMBER_0 = 0

        const val SECTION_NUMBER_1 = 1

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(
            sectionNumber: Int, listener: OnEulaReadListener
        ): EulaFragment {
            return EulaFragment(sectionNumber, listener)
        }
    }
}