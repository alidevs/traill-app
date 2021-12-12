package com.alidevs.traill.ui.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alidevs.traill.data.firebase.FirebaseService
import com.alidevs.traill.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		val firebaseService = FirebaseService.getInstance()

		binding.mainActivityEmailTextView.text = firebaseService.getCurrentUser()?.email ?: "N/A"
	}
}