package com.alidevs.traill.data.service

import android.util.Log
import com.alidevs.traill.data.model.Ride
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import io.reactivex.Completable
import io.reactivex.Observable


class FirestoreService private constructor() {
	
	private val db = FirebaseFirestore.getInstance()
	
	fun getRides() = Observable.create<List<Ride?>> { emitter ->
		val ridesList = mutableListOf<Ride?>()
		
		db.collection("rides")
			.whereEqualTo("status", "Open")
			.get()
			.addOnCompleteListener { task ->
				if (task.isSuccessful) {
					for (document in task.result!!) {
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
		val updatedRide = ride.apply {
			geoHash = GeoFireUtils.getGeoHashForLocation(
				GeoLocation(
					ride.origin!!.latitude,
					ride.origin!!.longitude
				)
			)
		}
		db.collection("rides")
			.add(updatedRide)
			.addOnCompleteListener { task ->
				if (task.isSuccessful) {
					emitter.onComplete()
				} else {
					emitter.onError(task.exception!!)
				}
			}
	}
	
	fun getNearbyRides(userLatLng: LatLng, radius: Float = 50 * 1000f) = Observable.create<Ride?> { emitter ->
		val bounds = GeoFireUtils.getGeoHashQueryBounds(
			GeoLocation(userLatLng.latitude, userLatLng.longitude),
			100.0
		)
		val tasks = mutableListOf<Task<QuerySnapshot>>()
		
		for (bound in bounds) {
			val query = db.collection("rides")
				.whereEqualTo("status", "Open")
				.orderBy("geoHash")
				.startAt(bound.startHash)
				.endAt(bound.endHash)
			
			tasks.add(query.get())
		}
		
		Tasks.whenAllComplete(tasks)
			.addOnCompleteListener {
				val matchingDocs = mutableListOf<DocumentSnapshot>()
				
				for (task in tasks) {
					val snap = task.result
					
					for (document in snap.documents) {
						val documentOriginGeoPoint = document.getGeoPoint("origin")!!
						val centerLocation = GeoLocation(userLatLng.latitude, userLatLng.longitude)
						val docLocation = GeoLocation(documentOriginGeoPoint.latitude, userLatLng.longitude)
						
						val distanceInMeters =
							GeoFireUtils.getDistanceBetween(docLocation, centerLocation)
						
						if (distanceInMeters <= radius) {
							matchingDocs.add(document)
							
							val ride = document.toObject(Ride::class.java)
							Log.i("FirestoreService", "getNearbyRides: $ride")
							ride?.let {
								emitter.onNext(it)
							}
						}
					}
				}
				Log.d("FirestoreService", "getNearbyRides: Emitter complete")
				emitter.onComplete()
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