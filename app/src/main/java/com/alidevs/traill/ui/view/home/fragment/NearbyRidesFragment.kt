package com.alidevs.traill.ui.view.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
		
		// list of colors
		val list = listOf(
			"red",
			"red",
			"red",
			"red",
			"red",
			"red",
			"red",
			"red",
			"red",
			"red",
			"red",
			"red"
		)
		binding.nearbyRidesRecyclerView.adapter = NearbyRidesAdapter(list)
		
		return binding.root
	}
	
	companion object {
		@JvmStatic
		fun newInstance() = NearbyRidesFragment()
	}
}