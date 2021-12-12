package com.alidevs.traill.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.alidevs.traill.databinding.FragmentLoginBinding
import com.alidevs.traill.ui.viewmodel.AuthViewModel
import com.alidevs.traill.utils.isValidEmail
import com.alidevs.traill.utils.isValidPassword

class LoginFragment : Fragment() {

	private lateinit var binding: FragmentLoginBinding
	private val viewModel: AuthViewModel by activityViewModels()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentLoginBinding.inflate(inflater, container, false)

		binding.loginButton.setOnClickListener { signInButtonPressed() }

		return binding.root
	}

	private fun signInButtonPressed() {
		val email = binding.loginEmailTextInputLayout.editText?.text.toString().trim()
		val password = binding.loginPasswordTextInputLayout.editText?.text.toString().trim()

		if (email.isValidEmail() && password.isValidPassword()) {
			viewModel.login(email, password)
		} else {
			Toast.makeText(activity, "Login Failed", Toast.LENGTH_SHORT).show()
		}
	}

	companion object {
		@JvmStatic
		fun newInstance() = LoginFragment()
	}
}
