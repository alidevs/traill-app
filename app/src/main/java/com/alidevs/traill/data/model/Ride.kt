package com.alidevs.traill.data.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.GeoPoint
import com.google.gson.Gson
import java.util.*

data class Ride(
	var id: String = UUID.randomUUID().toString(),
	var name: String? = null,
	var origin: GeoPoint? = null,
	var destination: GeoPoint? = null,
	var geoHash: String? = null,
	var distance: Double? = null,
	var fare: Double? = null,
	var riderId: String? = null,
	var status: String = "Open"
) {
	fun toJson(): String = Gson().toJson(this)
	
	companion object {
		fun fromJson(json: String): Ride = Gson().fromJson(json, Ride::class.java)
	}
}