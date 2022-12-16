package com.ham.onettsix.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager
import com.ham.onettsix.R
import com.ham.onettsix.databinding.DialogProgressBinding
import com.ham.onettsix.databinding.FragmentEulaBinding
import java.lang.Exception

class ProgressDialog : AppCompatDialogFragment() {

    var ctx: Context? = null

    private lateinit var binding: DialogProgressBinding


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
    ): View {
        binding = DialogProgressBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.setOnKeyListener(DialogInterface.OnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                return@OnKeyListener true
            }
            return@OnKeyListener false
        })
        return dialog
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