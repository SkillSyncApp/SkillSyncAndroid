package com.android.skillsync.services

import com.android.skillsync.models.serper.PlacesResponse
import retrofit.Call
import retrofit.http.Body
import retrofit.http.Headers
import retrofit.http.POST

interface PlacesApiService {
    @Headers(
        "Content-Type: application/json",
        "X-API-KEY: c39a6e95c0723e63321df9caceaf48a1531c6a8d"
    )
    @POST("places")
    fun getPlaces(@Body body: Map<String, Any>): Call<PlacesResponse>
}
