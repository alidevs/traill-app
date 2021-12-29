package com.alidevs.traill.data.model

import android.location.Location
import com.alidevs.traill.utils.ceil
import com.google.android.gms.maps.model.LatLng
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

data class Trip(
	var origin: LatLng? = null,
	var destination: LatLng? = null,
	val duration: Double? = null,
) {
	var distance: Double? = null
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
				resultInKilometers.toDouble().ceil()
			}
		}
	
	var fare: Double? = null
		get() = distance?.let { distance ->
			val d = distance * 3
			d.ceil()
		}
	
	override fun toString(): String {
		return "Trip(origin=$origin, destination=$destination, duration=$duration, distance=$distance, fare=$fare)"
	}
}
