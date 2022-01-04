package com.alidevs.traill.ui.home.RideDetails

import com.alidevs.traill.data.model.Ride

interface RideRequestListener {
	fun onRideRequested(ride: Ride)
}