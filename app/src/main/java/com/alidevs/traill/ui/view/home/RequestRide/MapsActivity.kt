package com.alidevs.traill.ui.view.home.RequestRide

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alidevs.traill.R
import com.alidevs.traill.data.enums.MapsAction
import com.alidevs.traill.databinding.ActivityMapsBinding
import com.alidevs.traill.utils.LocationService
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
	
	private lateinit var binding: ActivityMapsBinding
	private lateinit var mMap: GoogleMap
	private lateinit var locationService: LocationService
	
	private lateinit var pickupMarker: Marker
	private lateinit var destinationMarker: Marker
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		binding = ActivityMapsBinding.inflate(layoutInflater)
		setContentView(binding.root)
		
		locationService = LocationService.getInstance()
		
		val mapFragment = supportFragmentManager
			.findFragmentById(R.id.map) as SupportMapFragment
		mapFragment.getMapAsync(this)
		
		binding.mapsBackButton.setOnClickListener { finish() }
		
		binding.confirmLocationButton.setOnClickListener {
			finish()
		}
	}
	
	override fun onMapReady(googleMap: GoogleMap) {
		mMap = googleMap
		getLastKnownLocation()
		
		mMap.setOnMapClickListener { latLng ->
			if (::destinationMarker.isInitialized) destinationMarker.remove()
			
			val markerOptions = MarkerOptions().position(latLng).title("Destination location")
			destinationMarker = mMap.addMarker(markerOptions)!!
			mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.5f))
			
			val options = PolylineOptions().apply {
				color(Color.RED)
				width(5f)
			}
			
			updateUi(latLng)
			
			locationService.updateTrip(MapsAction.DESTINATION_LOCATION, latLng)
		}
	}
	
	// Get last known location and place marker on map
	private fun getLastKnownLocation(): LatLng {
		val lastKnownLocation = locationService.getLastKnownLocation(this)
		val latLng = LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)
		mMap.clear()
		val markerOptions = MarkerOptions().position(latLng).title("Current location")
		pickupMarker = mMap.addMarker(markerOptions)!!
		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.5f))
		updateUi(latLng)
		
		return latLng
	}
	
	// Update UI with current location
	private fun updateUi(currentLocation: LatLng) {
		binding.progressBar2.visibility = View.GONE
		binding.locationDetailsConstraintLayout.visibility = View.VISIBLE
		
		binding.currentLocationTextView.text =
			locationService.getAddressFromLocation(this@MapsActivity, currentLocation)
	}
	
}