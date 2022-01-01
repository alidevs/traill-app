package com.alidevs.traill.data.repository

import com.alidevs.traill.data.model.Ride
import com.alidevs.traill.data.service.FirestoreService

class FirestoreRepository {
	
	private val firestoreService = FirestoreService.getInstance()
	
	fun getRides() = firestoreService.getRides()

	fun createRide(ride: Ride) = firestoreService.createRide(ride)
	
}