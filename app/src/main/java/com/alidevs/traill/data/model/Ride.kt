package com.alidevs.traill.data.model

import com.google.firebase.firestore.GeoPoint
import com.google.gson.Gson

data class Ride(
	var name: String? = null,
	var origin: GeoPoint? = null,
	var destination: GeoPoint? = null,
	var geoHash: String? = null,
	var distance: Double? = null,
	var fare: Double? = null,
	var status: String = "Open"
) {
	fun toJson(): String = Gson().toJson(this)
	
	companion object {
		fun fromJson(json: String): Ride = Gson().fromJson(json, Ride::class.java)
	}
}