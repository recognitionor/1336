package com.ham.onettsix.dialog

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import com.ham.onettsix.R
import kotlinx.android.synthetic.main.dialog_two_button.view.*
import kotlinx.coroutines.*


class TwoButtonDialog(
    private var title: String,
    private var content: String,
    private var callback: (isPositive: Boolean, dialog: DialogFragment) -> Unit
) : AppCompatDialogFragment() {

    companion object {
        val TAG: String = TwoButtonDialog::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_two_button, container, true)
        view.two_button_title.text = title
        view.two_button_content.text = content
        view.two_button_positive.setOnClickListener {
            callback.invoke(true, this@TwoButtonDialog)
        }
        view.two_button_negative.setOnClickListener {
            callback.invoke(false, this@TwoButtonDialog)
        }
        return view
    }
}