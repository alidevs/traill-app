package com.alidevs.traill.data.model

import com.google.android.gms.maps.model.LatLng

data class Trip(
	var currentLocation: LatLng? = null,
	var destinationLocation: LatLng? = null,
	val distance: Double? = null,
	val duration: Double? = null,
) {
	override fun toString(): String {
		return "cl: $currentLocation, dl: $destinationLocation, di: $distance, du: $duration"
	}
}
