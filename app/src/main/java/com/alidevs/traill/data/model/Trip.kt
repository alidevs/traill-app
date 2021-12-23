package com.alidevs.traill.data.model

import android.location.Location
import com.google.android.gms.maps.model.LatLng

data class Trip(
	var origin: LatLng? = null,
	var destination: LatLng? = null,
	val duration: Double? = null,
) {
	val distance: Double?
		get() = origin?.let { origin ->
			destination?.let { destination ->
				val result = FloatArray(1)
				Location.distanceBetween(
					origin.latitude,
					origin.longitude,
					destination.latitude,
					destination.longitude,
					result
				)
				val resultInKilometers = result[0] / 1000
				resultInKilometers.toDouble()
			}
		}
	
	val fare: Double?
		get() = distance?.let { distance ->
			distance * 3
		}
}
