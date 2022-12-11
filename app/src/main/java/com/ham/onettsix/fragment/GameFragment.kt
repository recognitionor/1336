package com.ham.onettsix.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.ham.onettsix.LoginActivity
import com.ham.onettsix.R
import com.ham.onettsix.constant.ActivityResultKey
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.GameViewModel
import kotlinx.android.synthetic.main.fragment_game.*

class GameFragment : Fragment(R.layout.fragment_game) {

    private val gameViewModel by lazy {
        ViewModelProviders.of(
            this, ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity().applicationContext))
            )
        )[GameViewModel::class.java]
    }

    private val activityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == ActivityResultKey.LOGIN_RESULT_OK) {
                this@GameFragment.rps_game_fragment.getFragment<RPSGameFragment>().loginUpdate()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserve();
        gameViewModel.gameLoad()
    }

    private fun setupObserve() {
        gameViewModel.gameTypeInfo.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {

                    var remainTicket =
                        (it.data?.data?.allTicket ?: 0) - (it.data?.data?.usedTicket ?: 0)
                    if (remainTicket < 0) {
                        remainTicket = 0
                    }
                    (childFragmentManager.findFragmentById(R.id.ticket_status_fragment) as TicketStatusFragment)
                        .updateStatusText("$remainTicket")
                    (childFragmentManager.findFragmentById(R.id.rps_game_fragment) as RPSGameFragment)
                        .updateCountText(it.data?.data?.gameCount ?: 0, it.data?.data?.maxCount ?: 0)


                }
                else -> {}
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game, null)
    }

    fun login() {
        activityResult.launch(Intent(requireActivity(), LoginActivity::class.java))
    }

    fun updateMyTicket() {
        gameViewModel.gameTypeInfo.value?.data?.data?.let {
            var remainTicket = (it.allTicket) - it.usedTicket
            (childFragmentManager.findFragmentById(R.id.ticket_status_fragment) as TicketStatusFragment)
                .updateStatusText("${++remainTicket}")
        }
        gameViewModel.gameLoad()
    }
}
