package com.alidevs.traill.data.model

import com.alidevs.traill.data.model.directionsresponse.Distance
import com.google.firebase.firestore.GeoPoint

data class Ride (
	val name: String? = null,
	val origin: GeoPoint? = null,
	val destination: GeoPoint? = null,
	val distance: Double? = null,
	val fare: Double? = null
)
