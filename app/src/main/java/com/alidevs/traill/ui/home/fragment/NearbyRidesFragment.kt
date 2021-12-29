package com.alidevs.traill.ui.home.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.alidevs.traill.data.repository.FirestoreRepository
import com.alidevs.traill.databinding.FragmentNearbyRidesBinding
import com.alidevs.traill.ui.home.NearbyRidesAdapter
import io.reactivex.disposables.CompositeDisposable

class NearbyRidesFragment : Fragment() {
	
	private lateinit var binding: FragmentNearbyRidesBinding
	
	private lateinit var adapter: NearbyRidesAdapter
	
	private val disposabls = CompositeDisposable()
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentNearbyRidesBinding.inflate(inflater, container, false)
		
		adapter = NearbyRidesAdapter()
		binding.nearbyRidesRecyclerView.adapter = adapter
		
		val firestoreRepository = FirestoreRepository()
		
		val disposable = firestoreRepository.getRides()
			.subscribe { ridesList ->
				for (ride in ridesList) {
					ride?.let {
						adapter.addRide(it)
						Log.d("NearbyRidesFragment", "onCreateView: Added $it")
					}
				}
				
				binding.nearbyRidesProgressBar.visibility = View.GONE
			}
		
		disposabls.add(disposable)
		
		return binding.root
	}
	
	override fun onDetach() {
		super.onDetach()
		disposabls.clear()
	}
	
	companion object {
		@JvmStatic
		fun newInstance() = NearbyRidesFragment()
	}
}