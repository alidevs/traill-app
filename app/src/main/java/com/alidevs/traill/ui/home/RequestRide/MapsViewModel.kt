package com.alidevs.traill.ui.home.RequestRide

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alidevs.traill.data.model.directionsresponse.DirectionsResponse
import com.alidevs.traill.data.repository.GoogleDirectionsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapsViewModel : ViewModel() {
	
	private val repository = GoogleDirectionsRepository()
	
	fun getDirections(origin: String, destination: String): MutableLiveData<DirectionsResponse> {
		val liveData = MutableLiveData<DirectionsResponse>()
		
		CoroutineScope(Dispatchers.IO).launch {
			val response = repository.getDirections(origin, destination)
			Log.d("MapsViewModel", "getDirections: ${response.raw()}")
			
			if (!response.isSuccessful) {
				liveData.postValue(null)
				return@launch
			}
			
			response.body()?.let { directionsResponse ->
				liveData.postValue(directionsResponse)
			}
		}
		
		return liveData
	}
	
}