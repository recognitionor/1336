package com.jhlee.cleantest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private var useCase: UseCase) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(useCase as GetDataUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
