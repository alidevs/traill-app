package com.alidevs.traill.data.service

import android.util.Log
import com.alidevs.traill.data.model.Ride
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import io.reactivex.Observable

class FirestoreService private constructor() {
	
	private val db = FirebaseFirestore.getInstance()
	
	fun getRides() = Observable.create<List<Ride?>> { emitter ->
		val ridesList = mutableListOf<Ride?>()
		
		db.collection("rides")
			.get()
			.addOnCompleteListener { task ->
				if (task.isSuccessful) {
					for (document in task.result!!) {
						Log.d("FirestoreService", "getRides: ${document.id} => ${document.data}")
						val ride = document.toObject(Ride::class.java)
						ridesList.add(ride)
					}
					
					emitter.onNext(ridesList)
					emitter.onComplete()
				} else {
					emitter.onError(task.exception!!)
				}
			}
	}

    fun createRide(ride: Ride) = Completable.create { emitter ->
		db.collection("rides")
			.add(ride)
			.addOnCompleteListener { task ->
				if (task.isSuccessful) {
					emitter.onComplete()
				} else {
					emitter.onError(task.exception!!)
				}
			}
	}

    companion object {
		private var instance: FirestoreService? = null
		
		fun getInstance(): FirestoreService {
			if (instance == null) {
				instance = FirestoreService()
			}
			return instance!!
		}
	}
}