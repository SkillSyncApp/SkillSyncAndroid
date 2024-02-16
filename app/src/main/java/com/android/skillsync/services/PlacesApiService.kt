package com.android.skillsync.services

import com.android.skillsync.models.serper.PlacesResponse
import retrofit.Call
import retrofit.http.Body
import retrofit.http.Headers
import retrofit.http.POST

interface PlacesApiService {
    @Headers(
        "Content-Type: application/json",
        "X-API-KEY: 6ad2bb4f22fca025e6fad9dae09f951eb1c6ab71"
    )
    @POST("places")
    fun getPlaces(@Body body: Map<String, Any>): Call<PlacesResponse>
}
