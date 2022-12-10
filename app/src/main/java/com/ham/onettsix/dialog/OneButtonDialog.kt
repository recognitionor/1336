package com.ham.onettsix.dialog

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import com.ham.onettsix.R
import kotlinx.android.synthetic.main.dialog_one_button.view.*

class OneButtonDialog(
    private var title: String = "",
    private var content: String = "",
    private var callback: (dialog: DialogFragment) -> Unit
) :
    AppCompatDialogFragment() {

    companion object {
        val TAG: String = OneButtonDialog::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_one_button, container, true)
        view.one_button_title.text = title
        view.one_button_content.text = content
        view.one_button_btn.setOnClickListener { callback.invoke(this@OneButtonDialog) }
        return view
    }
}