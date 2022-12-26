package com.ham.onettsix.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.ham.onettsix.MainActivity
import com.ham.onettsix.R
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.databinding.FragmentGameBinding
import com.ham.onettsix.databinding.FragmentHomeBinding
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.GameViewModel

class GameFragment : Fragment() {

    private lateinit var binding: FragmentGameBinding

    private val gameViewModel by lazy {
        ViewModelProviders.of(
            this, ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity().applicationContext))
            )
        )[GameViewModel::class.java]
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
                    Log.d("jhlee", "SUCCESS : $remainTicket")
                    (childFragmentManager.findFragmentById(R.id.ticket_status_fragment) as TicketStatusFragment).updateStatusText(
                        "$remainTicket"
                    )
                    (childFragmentManager.findFragmentById(R.id.rps_game_fragment) as RPSGameFragment).apply {
                        this.updateCountText(
                            it.data?.data?.gameCount ?: 0, it.data?.data?.maxCount ?: 0
                        )
                        this.enableTicket(remainTicket > 0)
                    }

                }
                else -> {}
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameBinding.inflate(layoutInflater)
        return binding.root
    }

    fun login() {
        (activity as MainActivity).login()
    }

    fun updateMyTicket(needGameLoad: Boolean = false) {
        Log.d("jhlee", "updateMyTicket")
        gameViewModel.gameTypeInfo.value?.data?.data?.let {
            var remainTicket = (it.allTicket) - it.usedTicket
            (childFragmentManager.findFragmentById(R.id.ticket_status_fragment) as TicketStatusFragment).apply {
                this.updateStatusText(
                    "${++remainTicket}"
                )
            }
        }
        if (needGameLoad) {
            gameViewModel.gameLoad()
        }
    }
}
