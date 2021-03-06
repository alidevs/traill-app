package com.alidevs.traill.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.alidevs.traill.R
import com.alidevs.traill.data.service.AuthService
import com.alidevs.traill.databinding.ActivityMainBinding
import com.alidevs.traill.databinding.NavHeaderBinding
import com.alidevs.traill.ui.auth.AuthActivity
import com.alidevs.traill.ui.dashboard.DashboardFragment
import com.alidevs.traill.ui.home.HomeFragment
import com.alidevs.traill.ui.home.HomeViewModel
import com.alidevs.traill.ui.settings.SettingsFragment
import com.google.android.material.navigation.NavigationView
import com.tapadoo.alerter.Alerter


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
	
	private val viewModel: HomeViewModel by viewModels()
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
		
		val broadcastReceiver = object : BroadcastReceiver() {
			override fun onReceive(context: Context?, intent: Intent?) {
				val title = intent?.getStringExtra("title")
				val body = intent?.getStringExtra("body")
				
				if (title != null && body != null) {
					showNotificationDialog(title, body)
				}
			}
		}
		
		LocalBroadcastManager.getInstance(this)
			.registerReceiver(broadcastReceiver, IntentFilter("com.alidevs.traill.notification"))
	}
	
	private fun showNotificationDialog(title: String, body: String) {
		val alertOnClickListener = View.OnClickListener {
			Alerter.hide()
			Toast.makeText(this, "Alert clicked", Toast.LENGTH_SHORT).show()
		}
		
		Alerter
			.create(this)
			.setTitle(title)
			.setText(body)
			.setBackgroundColorRes(R.color.saffron_mango)
			.setTitleAppearance(R.style.AlertTextAppearance)
			.setTextAppearance(R.style.AlertTextAppearance)
			.setIcon(R.drawable.ic_carpool_icon)
			.setDuration(6500)
			.setOnClickListener(alertOnClickListener)
			.enableSwipeToDismiss()
			.show()
	}
	
	override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
		when (menuItem.itemId) {
			R.id.menu_home_item -> HomeFragment.newInstance()
//			R.id.menu_profile_item -> ProfileFragment.newInstance()
			R.id.settings_item -> SettingsFragment()
			R.id.driver_dashboard_item -> DashboardFragment.newInstance()
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