package com.alidevs.traill.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alidevs.traill.databinding.FragmentNavBarBinding

class NavBarFragment : Fragment() {

	 private lateinit var binding: FragmentNavBarBinding

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentNavBarBinding.inflate(inflater, container, false)

		 binding.navHamburgerMenuButton.setOnClickListener {
			 (activity as MainActivity).openDrawer()
		 }

		return binding.root
	}
	
	fun setTitle(title: String) {
		binding.navTitle.text = title
	}

	companion object {
		@JvmStatic
		fun newInstance() = NavBarFragment()
	}
}