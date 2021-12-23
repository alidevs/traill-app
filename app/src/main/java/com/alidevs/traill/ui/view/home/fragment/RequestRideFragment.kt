package com.alidevs.traill.ui.view.home.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.alidevs.traill.databinding.FragmentRequestRideBinding
import com.alidevs.traill.ui.view.home.RequestRide.MapsActivity
import com.alidevs.traill.utils.LocationService

class RequestRideFragment : Fragment() {
	
	private lateinit var binding: FragmentRequestRideBinding
	private lateinit var locationService: LocationService
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		// Inflate the layout for this fragment
		binding = FragmentRequestRideBinding.inflate(inflater, container, false)
		
		binding.currentLocationContainer.setOnClickListener { currentLocationContainerPressed() }
		binding.destinationLocationContainer.setOnClickListener { destinationLocationContainerPressed() }
		
		locationService = LocationService.instance
		locationService.getLastKnownLocation(requireActivity())
		
		return binding.root
	}
	
	override fun onStart() {
		super.onStart()
		setupRequestRideUi()
	}
	
	private fun setupRequestRideUi() {
		val trip = LocationService.trip
		val currentLocation = trip.origin
		val destinationLocation = trip.destination
		
		currentLocation?.let {
			val currentAddress = locationService.getAddressFromLocation(requireActivity(), it)
			binding.currentLocationAddressTextView.text = currentAddress
		}
		
		destinationLocation?.let {
			val destinationAddress = locationService.getAddressFromLocation(requireActivity(), it)
			binding.destinationLocationAddressTextView.text = destinationAddress
		}
	}
	
	private fun destinationLocationContainerPressed() {
		val intent = Intent(activity, MapsActivity::class.java)
		startActivity(intent)
	}
	
	private fun currentLocationContainerPressed() {
		Toast.makeText(
			activity,
			LocationService.trip.origin.toString(),
			Toast.LENGTH_SHORT
		).show()
	}
	
	companion object {
		@JvmStatic
		fun newInstance() = RequestRideFragment()
	}
}