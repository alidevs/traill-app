package com.alidevs.traill.ui.home.NearbyRides

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.alidevs.traill.R
import com.alidevs.traill.data.model.Ride
import com.alidevs.traill.data.repository.FirestoreRepository
import com.alidevs.traill.databinding.FragmentNearbyRidesBinding
import com.alidevs.traill.ui.home.RideDetails.RideDetailsFragment
import com.alidevs.traill.ui.home.RideDetails.RideRequestListener
import com.alidevs.traill.ui.main.MainActivity
import com.alidevs.traill.ui.main.NavBarFragment
import com.alidevs.traill.utils.helper.LocationHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class NearbyRidesFragment : Fragment(), RideRequestListener {
	
	private lateinit var binding: FragmentNearbyRidesBinding
	
	private lateinit var adapter: NearbyRidesAdapter
	
	private val disposables = CompositeDisposable()
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentNearbyRidesBinding.inflate(inflater, container, false)
		
		adapter = NearbyRidesAdapter(rideRequestListener = this)
		binding.nearbyRidesRecyclerView.adapter = adapter
		
		val firestoreRepository = FirestoreRepository()
		
		val lastKnownLocation = LocationHelper.lastKnownLocation
		
		if (lastKnownLocation != null) {
			firestoreRepository.getNearbyRides(lastKnownLocation)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe ({ ride ->
					adapter.addRide(ride)
					binding.nearbyRidesProgressBar.visibility = View.GONE
				}, { error ->
					Toast.makeText(activity, error.message, Toast.LENGTH_SHORT).show()
					binding.nearbyRidesProgressBar.visibility = View.GONE
				}).also { disposables.add(it) }
		} else {
			Toast.makeText(activity, "Location not found", Toast.LENGTH_SHORT).show()
		}
		return binding.root
	}
	
	override fun onRideRequested(ride: Ride) {
		val activity = requireActivity() as MainActivity
		val fragmentManager = activity.supportFragmentManager
		
		activity.supportFragmentManager.beginTransaction()
			.replace(R.id.main_fragment_container, RideDetailsFragment.newInstance(ride.toJson()))
			.addToBackStack(RideDetailsFragment.javaClass.name)
			.commit()
		
		fragmentManager.findFragmentById(R.id.main_nav_fragment_container)?.let {
			val navBarFragment = it as NavBarFragment
			navBarFragment.setTitle("Ride Details")
			navBarFragment.toggleNavigationButton(true)
		}
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