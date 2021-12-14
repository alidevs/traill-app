package com.alidevs.traill.ui.view.home.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alidevs.traill.databinding.FragmentNearbyRidesBinding
import com.alidevs.traill.ui.view.home.NearbyRidesAdapter

class NearbyRidesFragment : Fragment() {
	
	private lateinit var binding: FragmentNearbyRidesBinding
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		// Inflate the layout for this fragment
		binding = FragmentNearbyRidesBinding.inflate(inflater, container, false)
		
		val data = listOf("Red", "Green", "Blue", "Yellow", "Black", "White", "Pink", "Orange", "Purple", "Grey", "Brown", "Cyan", "Magenta")
		binding.nearbyRidesRecyclerView.adapter = NearbyRidesAdapter(data)
		
		return binding.root
	}
	
	companion object {
		@JvmStatic
		fun newInstance() = NearbyRidesFragment()
	}
}