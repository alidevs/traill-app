package com.alidevs.traill.data.model.directionsresponse

data class GeocodedWaypoint(
    val geocoder_status: String,
    val place_id: String,
    val types: List<String>
)