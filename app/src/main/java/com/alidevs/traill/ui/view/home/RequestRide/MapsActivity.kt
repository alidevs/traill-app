package com.alidevs.traill.ui.view.home.RequestRide

import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.alidevs.traill.R
import com.alidevs.traill.databinding.ActivityMapsBinding
import com.alidevs.traill.utils.LocationService
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
	
	private val viewModel: MapsViewModel by viewModels()
	private lateinit var binding: ActivityMapsBinding
	
	private lateinit var mMap: GoogleMap
	private lateinit var locationService: LocationService
	
	private lateinit var originMarker: Marker
	private lateinit var destinationMarker: Marker
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		binding = ActivityMapsBinding.inflate(layoutInflater)
		setContentView(binding.root)
		
		locationService = LocationService.instance
		
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
		updateUi()
		
		mMap.setOnMapClickListener { destinationLatLng ->
			if (::destinationMarker.isInitialized) destinationMarker.remove()
			binding.progressBar2.visibility = View.VISIBLE
			
			val originString = locationService.getLatLngString()
			val destinationString = "${destinationLatLng.latitude},${destinationLatLng.longitude}"
			
			val directionsLiveData =
				viewModel.getDirections(originString, destinationString)
			
			directionsLiveData.observe(this) { directionsResponse ->
				if (directionsResponse.status != "OK") {
					Log.i("MapsActivity", "onMapReady: ${directionsResponse.status}")
					return@observe
				}
				
				// Create a PolylineOptions object and add points
				drawRoute(directionsResponse.routes[0].overview_polyline.points)
				
				LocationService.trip.destination = destinationLatLng
				updateUi()
			}
			
		}
	}
	
	private fun drawRoute(points: String) {
		val polylineOptions = PolylineOptions().apply {
			// TODO: Remove lines from map when a new destination is selected
			addAll(PolyUtil.decode(points))
			width(8f)
			color(R.color.saffron_mango)
		}
		
		mMap.addPolyline(polylineOptions)
	}
	
	// Update UI with selected locations
	private fun updateUi() {
		binding.progressBar2.visibility = View.GONE
		val offset = 0.0065f
		
		with(LocationService.trip) {
			origin?.let { latLng ->
				val markerOptions = MarkerOptions().position(latLng).title("Current location")
				originMarker = mMap.addMarker(markerOptions)!!
				val cameraPosition = LatLng(latLng.latitude - offset, latLng.longitude)
				mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cameraPosition, 14.5f))
				
				binding.currentLocationAddressTextView.text =
					locationService.getAddressFromLocation(this@MapsActivity, latLng)
			}
			
			destination?.let { latLng ->
				val markerOptions = MarkerOptions().position(latLng).title("Destination location")
				destinationMarker = mMap.addMarker(markerOptions)!!
				val cameraPosition = LatLng(latLng.latitude - offset, latLng.longitude)
				mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cameraPosition, 14.5f))
				
				binding.destinationLocationAddressTextView.text =
					locationService.getAddressFromLocation(this@MapsActivity, latLng)
			}
			
			if (listOfNotNull(origin, destination).size == 2) {
				val distance = distance
				binding.distanceTextView.text = String.format("%.2f km", distance)
				binding.estimatePriceTextView.text = String.format("%.1f SR", fare)
			}
		}
	}
	
}