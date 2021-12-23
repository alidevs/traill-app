package com.alidevs.traill.ui.view.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alidevs.traill.databinding.NearbyRidesRowBinding

class NearbyRidesAdapter constructor(private var data: List<String> = listOf()) :
	RecyclerView.Adapter<NearbyRidesAdapter.ViewHolder>() {
	
	private lateinit var binding: NearbyRidesRowBinding
	
	init {
		setHasStableIds(true)
	}
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		binding = NearbyRidesRowBinding.inflate(inflater, parent, false)
		
		Log.d("NJ", "Hi")
		
		return ViewHolder(binding.root)
	}
	
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//		holder.bind(data[position])
		Log.d("NearbyRidesAdapter", "onBindViewHolder: $position")
	}
	
	override fun getItemCount(): Int {
		return data.size
	}
	
	override fun getItemId(position: Int) = position.toLong()
	
	override fun getItemViewType(position: Int) = position
	
	inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		fun bind(item: String) = with(binding) {
			// Set data to your item view here
			binding.driverNameTextView.text = item
			
			itemView.setOnClickListener {
				onClick(item)
			}
		}
		
		private fun onClick(item: String) {
		
		}
		
	}
	
}