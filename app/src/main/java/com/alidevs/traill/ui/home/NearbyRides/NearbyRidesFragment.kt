package com.alidevs.traill.ui.home.NearbyRides

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alidevs.traill.data.repository.FirestoreRepository
import com.alidevs.traill.utils.LocationHelper
import com.alidevs.traill.databinding.FragmentNearbyRidesBinding
import io.reactivex.disposables.CompositeDisposable

class NearbyRidesFragment : Fragment() {
	
	private lateinit var binding: FragmentNearbyRidesBinding
	
	private lateinit var adapter: NearbyRidesAdapter
	
	private val disposables = CompositeDisposable()
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentNearbyRidesBinding.inflate(inflater, container, false)
		
		adapter = NearbyRidesAdapter()
		binding.nearbyRidesRecyclerView.adapter = adapter
		
		val firestoreRepository = FirestoreRepository()
		
		val lastKnownLocation = LocationHelper.lastKnownLocation!!
		
		val disposable = firestoreRepository.getNearbyRides(lastKnownLocation)
			.subscribe { ride ->
				Log.d("NearbyRidesFragments", "onCreateView: $ride")
				adapter.addRide(ride)
				binding.nearbyRidesProgressBar.visibility = View.GONE
			}
		
		disposables.add(disposable)
		return binding.root
	}
	
	override fun onDetach() {
		super.onDetach()
		disposables.clear()
	}
	
	companion object {
		@JvmStatic
		fun newInstance() = NearbyRidesFragment()
	}
}