package com.ham.onettsix.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ham.onettsix.R
import kotlinx.android.synthetic.main.fragment_eula.*
import kotlin.math.abs


class EulaFragment(private val sectionNumber: Int, private val listener: OnEulaReadListener) :
    Fragment(R.layout.fragment_eula) {

    private var isReadDone = false

    interface OnEulaReadListener {
        fun onFinishRead(sectionNumber: Int)
        fun getCheckState(sectionNumber: Int): Boolean
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webview_eula.loadUrl("file:///android_res/raw/eula.html")
        isReadDone = listener.getCheckState(sectionNumber)
        webview_eula.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            val standardValue = webview_eula.contentHeight * webview_eula.scale
            val offset: Float = standardValue / 10

            val currentScroll = webview_eula.height + scrollY
            if (abs(currentScroll - standardValue) <= offset) {
                if (!isReadDone) {
                    isReadDone = true
                    listener.onFinishRead(sectionNumber)
                    Toast.makeText(
                        this@EulaFragment.context,
                        getString(R.string.confirm),
                        Toast.LENGTH_SHORT
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
            sectionNumber: Int,
            listener: OnEulaReadListener
        ): EulaFragment {
            return EulaFragment(sectionNumber, listener)
        }
    }
}