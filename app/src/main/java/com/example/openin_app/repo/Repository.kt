package com.example.openin_app.repo

import com.example.openin_app.mApiServices.ApiService
import com.example.openin_app.model.MainResponse
import retrofit2.Call
import retrofit2.Response

class Repository(private val apiService: ApiService) {
    suspend fun getData(): Response<MainResponse> {
        return apiService.getData()
    }
}
