package com.jhlee.cleantest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.jhlee.cleantest.databinding.ActivityMvvmBinding

class MVVMTestActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMvvmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataRepository = DataRepository()
        val getDataUseCase = GetDataUseCase(dataRepository)
        val viewModelFactory = ViewModelFactory(getDataUseCase)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        viewModel.fetchData()
        viewModel.data.observe(this) { data ->
        }
    }
}

