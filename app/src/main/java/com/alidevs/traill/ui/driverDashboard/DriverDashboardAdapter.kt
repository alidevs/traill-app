package com.alidevs.traill.ui.driverDashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alidevs.traill.data.model.Ride
import com.alidevs.traill.databinding.NearbyRidesRowBinding
import com.alidevs.traill.utils.getAddress

class DriverDashboardAdapter(private val data: MutableList<Ride> = mutableListOf()) :
	RecyclerView.Adapter<DriverDashboardAdapter.ViewHolder>() {
	
	// TODO: Change to DriverDashboardRowBinding
	private lateinit var binding: NearbyRidesRowBinding
	
	init {
		setHasStableIds(true)
	}
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		binding = NearbyRidesRowBinding.inflate(inflater, parent, false)
		
		return ViewHolder(binding.root)
	}
	
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(data[position])
	}
	
	override fun getItemCount(): Int {
		return data.size
	}
	
	override fun getItemId(position: Int) = position.toLong()
	
	override fun getItemViewType(position: Int) = position
	
	fun addRide(ride: Ride) {
		data.add(ride)
		notifyItemInserted(data.size - 1)
	}
	
	inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
		View.OnClickListener {
		
		fun bind(ride: Ride) = with(binding) {
			val context = itemView.context
			
			rowDriverNameTextView.text = ride.name
			rowDistanceTextView.text = "${ride.distance} km"
			rowPriceTextView.text = "${ride.fare} SR"
			rowDestinationTextView.text = ride.destination!!.getAddress(context)
			rowPickupPointTextView.text = ride.origin!!.getAddress(context)
			
			itemView.setOnClickListener(this@ViewHolder)
		}
		
		override fun onClick(v: View?) {
		
		}
		
	}
	
}