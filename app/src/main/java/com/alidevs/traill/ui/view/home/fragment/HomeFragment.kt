package com.alidevs.traill.ui.view.home.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alidevs.traill.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
	
	private lateinit var binding: FragmentHomeBinding
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentHomeBinding.inflate(inflater, container, false)
		
		return binding.root
	}
	
	companion object {
		@JvmStatic
		fun newInstance() = HomeFragment()
	}
}