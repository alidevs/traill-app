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

class RequestRideFragment : Fragment() {
	
	private lateinit var binding: FragmentRequestRideBinding
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		// Inflate the layout for this fragment
		binding = FragmentRequestRideBinding.inflate(inflater, container, false)
		
		binding.currentLocationContainer.setOnClickListener { currentLocationContainerPressed() }
		binding.destinationLocationContainer.setOnClickListener { destinationLocationContainerPressed() }
		
		return binding.root
	}
	
	private fun destinationLocationContainerPressed() {
		val intent = Intent(activity, MapsActivity::class.java)
		startActivity(intent)
	}
	
	private fun currentLocationContainerPressed() {
		Toast.makeText(activity, "Current location", Toast.LENGTH_SHORT).show()
	}
	
	companion object {
		@JvmStatic
		fun newInstance() = RequestRideFragment()
	}
}