package com.ham.onettsix.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager
import com.ham.onettsix.R
import java.lang.Exception

class ProgressDialog : AppCompatDialogFragment() {

    var ctx: Context? = null

    companion object {

        val TAG: String = ProgressDialog::class.java.simpleName

        lateinit var mSupportFragmentManager: FragmentManager

        fun getInstance(supportFragmentManager: FragmentManager): ProgressDialog =
            ProgressDialog().apply {
                mSupportFragmentManager = supportFragmentManager
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mainView = inflater.inflate(R.layout.dialog_progress, container, true).apply {
        }
        return mainView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes

        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun show() {
        show(mSupportFragmentManager, TAG)
    }

    override fun dismiss() {
        try {
            dismissAllowingStateLoss()
        } catch (ignored: Exception) {
        }

    }
}