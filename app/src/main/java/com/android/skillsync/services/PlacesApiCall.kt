package com.android.skillsync.services
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.skillsync.models.serper.Place
import com.android.skillsync.models.serper.PlacesResponse
import retrofit.Call
import retrofit.Callback
import retrofit.GsonConverterFactory
import retrofit.Response
import retrofit.Retrofit

private const val BASE_URL =
    "https://google.serper.dev/"
class PlacesApiCall {
    fun getPlacesByQuery(context: Context, query: String, callback: (Array<Place>?) -> Unit) {
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(
            GsonConverterFactory.create()).build()

        val apiService: PlacesApiService = retrofit.create<PlacesApiService>(PlacesApiService::class.java)

        val placesRequestBody = mapOf(
            "q" to query,
            "gl" to "il"
        )

        val call: Call<PlacesResponse> = apiService.getPlaces(placesRequestBody)
        call.enqueue(object: Callback<PlacesResponse> {
            override fun onResponse(response: Response<PlacesResponse>, retrofit: Retrofit?) {
                val res: PlacesResponse = response.body()
                Log.d("Succes", res.toString())
                callback(res.places)
            }

            override fun onFailure(t: Throwable?) {
                Toast.makeText(context, "Request Fail", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
