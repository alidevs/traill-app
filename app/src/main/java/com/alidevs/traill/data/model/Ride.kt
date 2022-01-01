package com.alidevs.traill.data.model

import com.google.firebase.firestore.GeoPoint

data class Ride(
    var name: String? = null,
    var origin: GeoPoint? = null,
    var destination: GeoPoint? = null,
    var distance: Double? = null,
    var fare: Double? = null
)
