package com.alidevs.traill.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.alidevs.traill.R
import com.alidevs.traill.data.service.AuthService
import com.alidevs.traill.databinding.ActivityMainBinding
import com.alidevs.traill.databinding.NavHeaderBinding
import com.alidevs.traill.ui.auth.AuthActivity
import com.alidevs.traill.ui.driverDashboard.DriverDashboardFragment
import com.alidevs.traill.ui.home.fragment.HomeFragment
import com.alidevs.traill.ui.home.fragment.NavBarFragment
import com.alidevs.traill.ui.profile.ProfileFragment
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
	
	private lateinit var binding: ActivityMainBinding
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)
		
		val authService = AuthService.getInstance()
		
		// Navigation Drawer header
		val drawerHeaderBinding =
			NavHeaderBinding.bind(binding.navigationView.getHeaderView(0))
		
		drawerHeaderBinding.navHeaderEmailTextView.text = authService.getCurrentUser()?.email
		drawerHeaderBinding.navHeaderFullnameTextView.text =
			authService.getCurrentUser()?.displayName
		
		drawerHeaderBinding.navHeaderLogoutButton.setOnClickListener { logoutButtonPressed() }
		
		binding.navigationView.setNavigationItemSelectedListener(this)
		
		if (authService.getCurrentUser() == null) {
			startActivity(Intent(this, AuthActivity::class.java))
			finish()
		}
	}
	
	override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
		when (menuItem.itemId) {
			R.id.menu_home_item -> HomeFragment.newInstance()
			R.id.menu_profile_item -> ProfileFragment.newInstance()
			R.id.driver_dashboard_item -> DriverDashboardFragment.newInstance()
			else -> null
		}?.let { fragment ->
			setToolbarTitle(menuItem.title.toString())
			
			supportFragmentManager.beginTransaction()
				.replace(R.id.main_fragment_container, fragment)
				.commit()
			
			menuItem.isChecked = true
			
			// Close the navigation drawer when an item is selected.
			binding.drawerLayout.closeDrawers()
			
			return true
		}
		
		return false
	}
	
	private fun logoutButtonPressed() {
		val alertDialog = AlertDialog.Builder(this)
		alertDialog.setTitle("Logout")
		alertDialog.setMessage("Are you sure you want to logout?")
		
		alertDialog.setPositiveButton("Yes") { _, _ ->
			AuthService.getInstance().logout()
			segueToAuthActivity()
		}
		
		alertDialog.setNeutralButton("Cancel") { _, _ ->
		}
		alertDialog.show()
	}
	
	private fun segueToAuthActivity() {
		val intent = Intent(this, AuthActivity::class.java)
		intent.putExtra("from_logout", true)
		startActivity(intent)
		finish()
	}
	
	private fun setToolbarTitle(title: String) {
		val navBarFragment =
			supportFragmentManager.findFragmentById(R.id.main_nav_fragment_container) as NavBarFragment
		navBarFragment.setTitle(title)
	}
	
	fun openDrawer() {
		binding.drawerLayout.openDrawer(GravityCompat.START)
	}
}