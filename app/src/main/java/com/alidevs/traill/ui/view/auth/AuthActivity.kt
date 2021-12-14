package com.alidevs.traill.ui.view.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.alidevs.traill.databinding.ActivityAuthBinding
import com.alidevs.traill.ui.view.home.MainActivity

class AuthActivity : AppCompatActivity(), AuthListener {

	private lateinit var binding: ActivityAuthBinding
	private val viewModel: AuthViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityAuthBinding.inflate(layoutInflater)
		setContentView(binding.root)

		viewModel.authListener = this
	}

	override fun onStarted() {
		binding.progressBar.visibility = View.VISIBLE
	}

	override fun onSuccess() {
		binding.progressBar.visibility = View.GONE
		segueToMainActivity()
	}

	override fun onFailure(message: String) {
		binding.progressBar.visibility = View.GONE
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
	}

	override fun onStart() {
		super.onStart()
		viewModel.user?.let {
			binding.progressBar.visibility = View.GONE
			segueToMainActivity()
		}
	}

	private fun segueToMainActivity() {
		Intent(this, MainActivity::class.java).also {
			it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
			startActivity(it)
		}
	}

}