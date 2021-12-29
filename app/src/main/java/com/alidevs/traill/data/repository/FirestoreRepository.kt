package com.alidevs.traill.data.repository

import com.alidevs.traill.data.service.FirestoreService

class FirestoreRepository {
	
	private val firestoreService = FirestoreService.getInstance()
	
	fun getRides() = firestoreService.getRides()
	
}