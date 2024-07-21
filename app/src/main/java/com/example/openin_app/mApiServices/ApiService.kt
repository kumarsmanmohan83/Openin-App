package com.example.openin_app.mApiServices

import com.example.openin_app.model.MainResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("api/v1/dashboardNew")
    suspend fun getData(): Response<MainResponse>
}