package com.alidevs.traill.ui.home.RequestRide

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alidevs.traill.data.model.Ride
import com.alidevs.traill.data.model.directionsresponse.DirectionsResponse
import com.alidevs.traill.data.repository.FirestoreRepository
import com.alidevs.traill.data.repository.GoogleDirectionsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapsViewModel : ViewModel() {
	
	private val googleDirectionsRepository = GoogleDirectionsRepository()
	private val firestoreRepository = FirestoreRepository()

	private val disposables = CompositeDisposable()
	
	fun getDirections(origin: String, destination: String): MutableLiveData<DirectionsResponse> {
		val liveData = MutableLiveData<DirectionsResponse>()
		
		CoroutineScope(Dispatchers.IO).launch {
			val response = googleDirectionsRepository.getDirections(origin, destination)
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

	fun createRide(ride: Ride) {
		val disposable = firestoreRepository.createRide(ride)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe({
				Log.d("MapsViewModel", "createRide: success")
			}, {
				Log.d("MapsViewModel", "createRide: ${it.message}")
			})

		disposables.add(disposable)
	}

	override fun onCleared() {
		super.onCleared()
		disposables.clear()
	}
}