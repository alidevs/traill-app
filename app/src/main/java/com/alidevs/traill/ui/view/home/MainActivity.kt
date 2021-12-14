package com.alidevs.traill.ui.view.home

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.alidevs.traill.R
import com.alidevs.traill.data.firebase.FirebaseService
import com.alidevs.traill.databinding.ActivityMainBinding
import com.alidevs.traill.databinding.NavHeaderBinding
import com.alidevs.traill.ui.view.auth.AuthActivity
import com.alidevs.traill.ui.view.home.fragment.HomeFragment
import com.alidevs.traill.ui.view.home.fragment.NavBarFragment
import com.alidevs.traill.ui.view.profile.ProfileFragment


class MainActivity : AppCompatActivity() {
	
	private lateinit var binding: ActivityMainBinding
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)
		
		val firebaseService = FirebaseService.getInstance()
		
		// Navigation Drawer header
		val drawerHeaderBinding =
			NavHeaderBinding.bind(binding.navigationView.getHeaderView(0))
		
		drawerHeaderBinding.navHeaderEmailTextView.text = firebaseService.getCurrentUser()?.email
		drawerHeaderBinding.navHeaderFullnameTextView.text =
			firebaseService.getCurrentUser()?.displayName
		
		drawerHeaderBinding.navHeaderLogoutButton.setOnClickListener { logoutButtonPressed() }
		
		setupDrawerContent()
		
	}
	
	private fun logoutButtonPressed() {
		val alertDialog = AlertDialog.Builder(this)
		alertDialog.setTitle("Logout")
		alertDialog.setMessage("Are you sure you want to logout?")
		
		alertDialog.setPositiveButton("Yes") { _, _ ->
			FirebaseService.getInstance().logout()
			segueToAuthActivity()
		}
		
		alertDialog.setNeutralButton("Cancel") { _, _ ->
		}
		alertDialog.show()
	}
	
	private fun segueToAuthActivity() {
		val intent = Intent(this, AuthActivity::class.java)
		startActivity(intent)
		finish()
	}
	
	private fun setupDrawerContent() {
		binding.navigationView.setNavigationItemSelectedListener { menuItem ->
			selectDrawerItem(menuItem)
			true
		}
	}
	
	private fun selectDrawerItem(menuItem: MenuItem) {
		val fragment = when (menuItem.itemId) {
			R.id.menu_profile_item -> {
				setToolbarTitle(menuItem.title.toString())
				ProfileFragment.newInstance()
			}
			else -> {
				setToolbarTitle(menuItem.title.toString())
				HomeFragment.newInstance()
			}
		}
		
		supportFragmentManager.beginTransaction()
			.replace(R.id.main_fragment_container, fragment)
			.commit()
		
		menuItem.isChecked = true
		
		// Close the navigation drawer when an item is selected.
		binding.drawerLayout.closeDrawers()
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