package com.alidevs.traill.ui.home.RequestRide

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.alidevs.traill.R
import com.alidevs.traill.data.model.Ride
import com.alidevs.traill.data.model.Trip
import com.alidevs.traill.data.repository.AuthRepository
import com.alidevs.traill.databinding.ActivityMapsBinding
import com.alidevs.traill.utils.helper.LocationHelper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.firestore.GeoPoint
import com.google.maps.android.PolyUtil
import io.reactivex.disposables.CompositeDisposable

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
	
	private val disposables = CompositeDisposable()
	private val viewModel: MapsViewModel by viewModels()
	private lateinit var binding: ActivityMapsBinding
	
	private lateinit var mMap: GoogleMap
	private lateinit var locationHelper: LocationHelper
	
	private lateinit var polyline: Polyline
	private lateinit var originMarker: Marker
	private lateinit var destinationMarker: Marker
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		binding = ActivityMapsBinding.inflate(layoutInflater)
		setContentView(binding.root)
		
		locationHelper = LocationHelper.instance
		
		val mapFragment = supportFragmentManager
			.findFragmentById(R.id.map) as SupportMapFragment
		mapFragment.getMapAsync(this)
		
		binding.mapsBackButton.setOnClickListener { finish() }
		
		binding.confirmLocationButton.setOnClickListener {
			val userDisplayName = AuthRepository().getCurrentUser()!!.displayName
			val trip = LocationHelper.trip
			val ride = Ride().apply {
				name = userDisplayName
				origin = GeoPoint(trip.origin!!.latitude, trip.origin!!.longitude)
				destination = GeoPoint(trip.destination!!.latitude, trip.destination!!.longitude)
				distance = trip.distance
				fare = trip.fare
			}
			
			viewModel.createRide(ride)
				.subscribe({
					Toast.makeText(this, "createRide done", Toast.LENGTH_SHORT).show()
					LocationHelper.trip = Trip()
					finish()
				}, {
					Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
				}).also { disposables.add(it) }
		}
	}
	
	override fun onMapReady(googleMap: GoogleMap) {
		mMap = googleMap
		updateUi()
		
		if (::destinationMarker.isInitialized) drawRoute()
		
		mMap.setOnMapClickListener { destinationLatLng ->
			if (::destinationMarker.isInitialized) destinationMarker.remove()
			binding.progressBar2.visibility = View.VISIBLE
			LocationHelper.trip.destination = destinationLatLng
			drawRoute()
		}
	}
	
	private fun drawRoute() {
		val trip = LocationHelper.trip
		val originString = locationHelper.getLatLngString()
		val destinationString = "${trip.destination!!.latitude},${trip.destination!!.longitude}"
		
		val directionsLiveData =
			viewModel.getDirections(originString, destinationString)
		
		directionsLiveData.observe(this) { directionsResponse ->
			if (directionsResponse.status != "OK") {
				Log.i("MapsActivity", "onMapReady: ${directionsResponse.status}")
				return@observe
			}
			
			val points = directionsResponse.routes[0].overview_polyline.points
			addPolyline(points)
			
			updateUi()
		}
	}
	
	private fun addPolyline(points: String) {
		if (::polyline.isInitialized) polyline.remove()
		
		val polylineOptions = PolylineOptions().apply {
			addAll(PolyUtil.decode(points))
			width(8f)
			color(ContextCompat.getColor(this@MapsActivity, R.color.saffron_mango))
		}
		
		polyline = mMap.addPolyline(polylineOptions)
	}
	
	// Update UI with selected locations
	private fun updateUi() {
		binding.progressBar2.visibility = View.GONE
		val offset = 0.0065f
		
		with(LocationHelper.trip) {
			origin?.let { latLng ->
				val markerOptions = MarkerOptions().position(latLng).title("Current location")
				originMarker = mMap.addMarker(markerOptions)!!
				val cameraPosition = LatLng(latLng.latitude - offset, latLng.longitude)
				mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cameraPosition, 14.5f))
				
				binding.currentLocationAddressTextView.text =
					locationHelper.getAddressFromLocation(this@MapsActivity, latLng)
			}
			
			destination?.let { latLng ->
				val markerOptions = MarkerOptions().position(latLng).title("Destination location")
				destinationMarker = mMap.addMarker(markerOptions)!!
				val cameraPosition = LatLng(latLng.latitude - offset, latLng.longitude)
				mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cameraPosition, 14.5f))
				
				binding.destinationLocationAddressTextView.text =
					locationHelper.getAddressFromLocation(this@MapsActivity, latLng)
			}
			
			if (listOfNotNull(origin, destination).size == 2) {
				val distance = distance
				binding.distanceTextView.text = String.format("%.2f km", distance)
				binding.estimatePriceTextView.text = String.format("%.1f SR", fare)
			}
		}
	}
	
}