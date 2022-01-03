package com.alidevs.traill.utils

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint

fun GeoPoint.getAddress(context: Context): String {
	val latLng = LatLng(this.latitude, this.longitude)
	return LocationHelper.instance.getAddressFromLocation(context, latLng)
}