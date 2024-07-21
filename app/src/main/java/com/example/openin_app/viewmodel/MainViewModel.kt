package com.example.openin_app.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.openin_app.mApiServices.RetrofitClient
import com.example.openin_app.model.MainResponse
import com.example.openin_app.repo.Repository
import kotlinx.coroutines.launch
import retrofit2.Call

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Repository
    private val _data = MutableLiveData<MainResponse>()
    val data: LiveData<MainResponse> get() = _data

    init {
        val apiService = RetrofitClient.create(application)
        repository = Repository(apiService)
    }

    fun fetchData() {
        viewModelScope.launch {
            try {
                val response = repository.getData()
                if (response.isSuccessful) {
                    _data.value = response.body()
                } else {
                    Log.e("API_ERROR", "Error: ${response.code()} ${response.message()}")
                    Toast.makeText(getApplication(), "Something went wrong!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("API_EXCEPTION", "Exception: ${e.message}")
                Toast.makeText(getApplication(), "Something went wrong!", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
