package com.alidevs.traill.ui.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alidevs.traill.R

class NavBarFragment : Fragment() {

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_nav_bar, container, false)
	}

	companion object {
		@JvmStatic
		fun newInstance() = NavBarFragment()
	}
}