package com.alidevs.traill.ui.view.home.RequestRide

import android.os.Bundle
import android.telecom.Call
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alidevs.traill.R
import com.alidevs.traill.databinding.ActivityMapsBinding
import com.alidevs.traill.utils.LocationService
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
	
	private lateinit var binding: ActivityMapsBinding
	private lateinit var mMap: GoogleMap
	private lateinit var locationService: LocationService
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		binding = ActivityMapsBinding.inflate(layoutInflater)
		setContentView(binding.root)
		
		locationService = LocationService.getInstance()
		
		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		val mapFragment = supportFragmentManager
			.findFragmentById(R.id.map) as SupportMapFragment
		mapFragment.getMapAsync(this)
		
		binding.mapsBackButton.setOnClickListener {
			finish()
		}
	}
	
	override fun onMapReady(googleMap: GoogleMap) {
		mMap = googleMap
		getLastKnownLocation()
		
		mMap.setOnMapClickListener { latLng ->
			mMap.clear()
			mMap.addMarker(MarkerOptions().position(latLng)).apply {
				title = locationService.getAddressFromLocation(this@MapsActivity, latLng)
			}
			placeMarker(latLng)
			updateUi(latLng)
		}
	}
	
	// Get last known location and place marker on map
	private fun getLastKnownLocation() {
		val lastKnownLocation = locationService.getLastKnownLocation(this)
		val latLng = LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)
		placeMarker(latLng)
		updateUi(latLng)
	}
	
	// Update UI with current location
	private fun updateUi(currentLocation: LatLng) {
		binding.progressBar2.visibility = View.GONE
		binding.locationDetailsConstraintLayout.visibility = View.VISIBLE
		
		binding.currentLocationTextView.text =
			locationService.getAddressFromLocation(this@MapsActivity, currentLocation)
	}
	
	// Place the marker at location
	private fun placeMarker(location: LatLng) {
		mMap.clear()
		mMap.addMarker(MarkerOptions().position(location).title("Your Location"))
		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 14.5f))
		
		updateUi(location)
	}
	
}