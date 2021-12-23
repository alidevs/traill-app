package com.alidevs.traill.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.alidevs.traill.data.model.Trip
import com.google.android.gms.maps.model.LatLng
import java.io.IOException

class LocationService {
	
	companion object {
		var instance = LocationService()
		var trip = Trip()
		
		var lastKnownLocation: LatLng? = null
			set(value) {
				field = value
				if (field != null) {
					trip.origin = value
				}
			}
	}
	
	fun getLastKnownLocation(activity: Activity): LatLng {
		checkLocationPermission(activity)
		
		val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
		var location = LatLng(0.0, 0.0)
		val providers = locationManager.getProviders(true)
		var bestLocation: Location? = null
		
		for (provider in providers) {
			val l = locationManager.getLastKnownLocation(provider) ?: continue
			if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
				// Found best last known location
				bestLocation = l
			}
		}
		
		bestLocation?.let {
			val latLng = LatLng(it.latitude, it.longitude)
			location = latLng
			lastKnownLocation = location
		}
		
		return location
	}
	
	// Get address from location
	fun getAddressFromLocation(context: Context, location: LatLng): String {
		val geocoder = Geocoder(context)
		var address: String? = null
		
		try {
			val addresses: List<Address> =
				geocoder.getFromLocation(location.latitude, location.longitude, 1)
			if (addresses.isNotEmpty()) {
				address = addresses[0].getAddressLine(0)
			}
		} catch (e: IOException) {
			e.printStackTrace()
		}
		
		return address ?: "No address found"
	}
	
	// Check if user granted fine location permission, if not request it
	private fun checkLocationPermission(activity: Activity) {
		if (ActivityCompat.checkSelfPermission(
				activity,
				android.Manifest.permission.ACCESS_FINE_LOCATION
			) != PackageManager.PERMISSION_GRANTED
		) {
			ActivityCompat.requestPermissions(
				activity,
				arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
				1
			)
		}
		
	}
	
	fun getLatLngString(): String {
		return trip.origin?.latitude.toString() + "," + (trip.origin?.longitude)
	}
	
}