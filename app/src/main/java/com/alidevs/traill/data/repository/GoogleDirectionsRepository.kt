package com.alidevs.traill.data.repository

import com.alidevs.traill.data.api.GoogleDirectionsApi

class GoogleDirectionsRepository {
	
	private val googleDirectionsService = GoogleDirectionsApi.getInstance()
	
	suspend fun getDirections(origin: String, destination: String) =
		googleDirectionsService.getDirections(origin, destination)
	
}