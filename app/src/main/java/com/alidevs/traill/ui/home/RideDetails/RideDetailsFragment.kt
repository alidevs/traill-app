package com.alidevs.traill.ui.home.RideDetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.alidevs.traill.data.model.Ride
import com.alidevs.traill.databinding.FragmentRideDetailsBinding
import com.alidevs.traill.ui.home.HomeViewModel
import com.alidevs.traill.utils.getAddress
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RideDetailsFragment : Fragment() {
	
	private val disposables = CompositeDisposable()
	private val viewModel: HomeViewModel by activityViewModels()
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
		
		with(binding) {
			ride?.let { ride ->
				carDetailsLinearLayout.visibility = View.GONE
				nameTextView.text = ride.name
				totalPriceTextView.text = "${ride.fare} SR"
				pickupPointTextView.text = ride.origin!!.getAddress(requireActivity())
				destinationTextView.text = ride.destination!!.getAddress(requireActivity())
				
				acceptRideButton.setOnClickListener {
					viewModel.requestRide(ride)
						.subscribeOn(Schedulers.io())
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe({
							Toast.makeText(activity, "Ride requested", Toast.LENGTH_SHORT).show()
							requireActivity().supportFragmentManager.popBackStackImmediate()
						}, { error ->
							error.printStackTrace()
							Log.e("RideDetailsFragment", "onCreateView: ${error.message}", error)
						}).also { disposables.add(it) }
				}
			}
		}
		
		return binding.root
	}
	
	override fun onDestroy() {
		super.onDestroy()
		disposables.clear()
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