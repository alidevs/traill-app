package com.alidevs.traill.ui.home.RequestRide

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.alidevs.traill.databinding.FragmentRequestRideBinding
import com.alidevs.traill.utils.LocationHelper

class RequestRideFragment : Fragment() {
	
	private lateinit var binding: FragmentRequestRideBinding
	private lateinit var locationHelper: LocationHelper
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		// Inflate the layout for this fragment
		binding = FragmentRequestRideBinding.inflate(inflater, container, false)
		
		binding.currentLocationContainer.setOnClickListener { currentLocationContainerPressed() }
		binding.destinationLocationContainer.setOnClickListener { destinationLocationContainerPressed() }
		
		locationHelper = LocationHelper.instance
		locationHelper.getLastKnownLocation(requireActivity())
		
		return binding.root
	}
	
	override fun onStart() {
		super.onStart()
		setupRequestRideUi()
	}
	
	private fun setupRequestRideUi() {
		val trip = LocationHelper.trip
		val currentLocation = trip.origin
		val destinationLocation = trip.destination
		
		currentLocation?.let {
			val currentAddress = locationHelper.getAddressFromLocation(requireActivity(), it)
			binding.currentLocationAddressTextView.text = currentAddress
		}
		
		destinationLocation?.let {
			val destinationAddress = locationHelper.getAddressFromLocation(requireActivity(), it)
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
			LocationHelper.trip.toString(),
			Toast.LENGTH_SHORT
		).show()
		
		Log.i("RequestRideFragment", "currentLocationContainerPressed: ${LocationHelper.trip}")
	}
	
	companion object {
		@JvmStatic
		fun newInstance() = RequestRideFragment()
	}
}