package com.alidevs.traill.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alidevs.traill.data.repository.FirestoreRepository
import com.alidevs.traill.databinding.FragmentDashboardBinding
import io.reactivex.disposables.CompositeDisposable

class DashboardFragment : Fragment() {
	
	private lateinit var binding: FragmentDashboardBinding
	
	private lateinit var adapter: DashboardAdapter
	
	private val disposables = CompositeDisposable()
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentDashboardBinding.inflate(inflater, container, false)
		adapter = DashboardAdapter()
		
		binding.driverDashboardRecyclerView.adapter = adapter
		
		val firestoreRepository = FirestoreRepository()
		
		val disposable = firestoreRepository.getRides()
			.subscribe { ridesList ->
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
		fun newInstance() = DashboardFragment()
	}
}