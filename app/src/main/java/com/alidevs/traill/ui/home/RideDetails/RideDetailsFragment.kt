package com.alidevs.traill.ui.home.RideDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alidevs.traill.data.model.Ride
import com.alidevs.traill.databinding.FragmentRideDetailsBinding

class RideDetailsFragment : Fragment() {
	
	private lateinit var binding: FragmentRideDetailsBinding
	private var ride: Ride? = null
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		arguments?.let {
			val rideJson = it.getString("ride")!!
			ride = Ride.fromJson(rideJson)
		}
	}
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentRideDetailsBinding.inflate(inflater, container, false)
		
		
		
		return binding.root
	}
	
	companion object {
		@JvmStatic
		fun newInstance(rideJson: String) =
			RideDetailsFragment().apply {
				arguments = Bundle().apply {
					putString("ride", rideJson)
				}
			}
	}
}