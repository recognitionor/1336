package com.jhlee.cleantest

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel(private val getDataUseCase: GetDataUseCase) : ViewModel() {
    private val _data = MutableLiveData<String>()
    val data: LiveData<String>
        get() = _data

    fun fetchData() {
        val data = getDataUseCase.execute()
        Log.d("jhlee", "fetch : $data")
        _data.value = data
    }
}

class GetDataUseCase(private val dataRepository: DataRepository) : UseCase() {
    fun execute(): String {
        return dataRepository.getData()
    }
}

class DataRepository {
    fun getData(): String {
        // 데이터 가져오는 로직 작성
        return "Hello, Clean Architecture!"
    }
}
