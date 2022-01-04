package com.alidevs.traill.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alidevs.traill.R
import com.alidevs.traill.databinding.FragmentNavBarBinding

class NavBarFragment : Fragment() {
	
	private lateinit var binding: FragmentNavBarBinding
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentNavBarBinding.inflate(inflater, container, false)
		
		val activity = requireActivity() as MainActivity
		
		binding.navHamburgerMenuButton.setOnClickListener {
			activity.openDrawer()
		}
		
		binding.navBackButton.setOnClickListener {
			activity.supportFragmentManager.popBackStackImmediate()
			setTitle("Home")
			toggleNavigationButton(false)
		}
		
		return binding.root
	}
	
	fun setTitle(title: String) {
		binding.navTitle.text = title
	}
	
	fun toggleNavigationButton(show: Boolean) {
		binding.navBackButton.visibility = if (show) View.VISIBLE else View.GONE
		binding.navHamburgerMenuButton.visibility = if (show) View.GONE else View.VISIBLE
	}
	
	companion object {
		@JvmStatic
		fun newInstance() = NavBarFragment()
	}
}