package com.alidevs.traill.ui.home

import androidx.lifecycle.ViewModel
import com.alidevs.traill.data.model.Ride
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable

class HomeViewModel : ViewModel() {
	
	private val firestore = FirebaseFirestore.getInstance()
	private val auth = FirebaseAuth.getInstance()
	
	fun requestRide(ride: Ride) = Completable.create { emitter ->
		firestore
			.collection("rides")
			.document(ride.id)
			.update("status", "requested", "riderId", auth.currentUser!!.uid)
			.addOnSuccessListener {
				emitter.onComplete()
			}
			.addOnFailureListener {
				emitter.onError(it)
			}
	}
	
}