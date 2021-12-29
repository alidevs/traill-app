package com.alidevs.traill.ui.driverDashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.alidevs.traill.data.repository.FirestoreRepository
import com.alidevs.traill.databinding.FragmentDriverDashboardBinding
import io.reactivex.disposables.CompositeDisposable

class DriverDashboardFragment : Fragment() {
	
	private lateinit var binding: FragmentDriverDashboardBinding
	
	private lateinit var adapter: DriverDashboardAdapter
	
	private val disposables = CompositeDisposable()
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentDriverDashboardBinding.inflate(inflater, container, false)
		adapter = DriverDashboardAdapter()
		
		binding.driverDashboardRecyclerView.adapter = adapter
		
		val firestoreRepository = FirestoreRepository()
		
		val disposable = firestoreRepository.getRides()
			.subscribe { ridesList ->
				Toast.makeText(activity, "${ridesList.size}", Toast.LENGTH_SHORT).show()
				for (ride in ridesList) {
					ride?.let {
						adapter.addRide(it)
					}
				}
				
				binding.driverDashboardProgressBar.visibility = View.GONE
			}
		
		disposables.add(disposable)
		
		return binding.root
	}
	
	override fun onDetach() {
		super.onDetach()
		disposables.dispose()
	}
	
	companion object {
		@JvmStatic
		fun newInstance() = DriverDashboardFragment()
	}
}