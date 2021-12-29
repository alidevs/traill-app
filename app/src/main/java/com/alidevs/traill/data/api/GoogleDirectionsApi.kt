package com.alidevs.traill.data.api

import com.alidevs.traill.BuildConfig
import com.alidevs.traill.data.model.directionsresponse.DirectionsResponse
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleDirectionsApi {
	
	@GET("json")
	suspend fun getDirections(
		@Query("origin") origin: String,
		@Query("destination") destination: String
	): Response<DirectionsResponse>
	
	companion object {
		private var instance: GoogleDirectionsApi? = null
		
		fun getInstance(): GoogleDirectionsApi {
			if (instance == null) {
				val okHttpClient = OkHttpClient.Builder()
					.addInterceptor { chain ->
						val url =
							chain.request().url().newBuilder()
								.addQueryParameter(
									"key",
									BuildConfig.GOOGLE_API_KEY
								)
								.build()
						chain.proceed(chain.request().newBuilder().url(url).build())
					}.build()
				
				instance = Retrofit.Builder()
					.baseUrl("https://maps.googleapis.com/maps/api/directions/")
					.addConverterFactory(GsonConverterFactory.create())
					.client(okHttpClient)
					.build()
					.create(GoogleDirectionsApi::class.java)
			}
			return instance!!
		}
	}
	
}