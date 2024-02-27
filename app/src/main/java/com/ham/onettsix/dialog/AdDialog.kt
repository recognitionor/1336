package com.ham.onettsix.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.ham.onettsix.R
import com.ham.onettsix.ad.AdManager
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.databinding.DialogAdLayoutBinding
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.VideoViewModel

class AdDialog(
    private var title: String = "",
    private var content: String = "",
    private var callback: (dialog: DialogFragment) -> Unit
) : AppCompatDialogFragment(), AdManager.AdManagerListener {

    private lateinit var binding: DialogAdLayoutBinding

    private val videoViewModel by lazy {
        ViewModelProviders.of(
            this, ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity().applicationContext))
            )
        )[VideoViewModel::class.java]
    }

    companion object {
        val TAG: String = AdDialog::class.java.simpleName
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObserver()
    }

    private fun setObserver() {
        videoViewModel.typingRvConfig.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (activity != null) {
                        binding.adDialogLayout.visibility = View.GONE
                        binding.adLoadProgress.visibility = View.VISIBLE
                        it.data?.data?.let { videoSignature ->
                            AdManager.getInstance().load(videoSignature, activity!!, this)
                        }
                    }
                }

                Status.LOADING -> {
                }

                Status.ERROR -> {
                }
            }
        }
        videoViewModel.videoSignature.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { video ->
                        AdManager.getInstance().load(video.data, requireActivity(), this)
                    }
                }

                Status.ERROR -> {
                    binding.adDialogLayout.visibility = View.VISIBLE
                    binding.adLoadProgress.visibility = View.GONE
                }

                Status.LOADING -> {
                    binding.adDialogLayout.visibility = View.GONE
                    binding.adLoadProgress.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DialogAdLayoutBinding.inflate(layoutInflater)
        binding.oneButtonTitle.text = title
        binding.oneButtonContent.text = content
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
        binding.oneButtonBtn.setOnClickListener {
            dialog?.dismiss()
        }
        binding.adGoBtn.setOnClickListener {
            videoViewModel.getRvConfig()
        }
        return binding.root
    }
    override fun onSuccessLoaded() {
        binding.adDialogLayout.visibility = View.VISIBLE
        binding.adLoadProgress.visibility = View.GONE
    }

    override fun onFailLoaded(error: String) {
        binding.adDialogLayout.visibility = View.VISIBLE
        binding.adLoadProgress.visibility = View.GONE
        Toast.makeText(activity, resources.getString(R.string.ad_dialog_no_ad), Toast.LENGTH_SHORT)
            .show()
        dismiss()
    }

    override fun onClosed() {
        callback.invoke(this)
        this.dialog?.dismiss()
    }
}