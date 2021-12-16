package com.alidevs.traill.ui.view.auth.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.alidevs.traill.R
import com.alidevs.traill.data.model.UserModel
import com.alidevs.traill.databinding.FragmentRegisterBinding
import com.alidevs.traill.ui.view.auth.AuthViewModel
import com.alidevs.traill.data.enums.Role
import com.alidevs.traill.utils.isValidEmail
import com.alidevs.traill.utils.isValidFullname
import com.alidevs.traill.utils.isValidPassword

class RegisterFragment : Fragment() {

	private lateinit var binding: FragmentRegisterBinding
	private val viewModel by activityViewModels<AuthViewModel>()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentRegisterBinding.inflate(inflater, container, false)

		binding.singUpButton.setOnClickListener { signUpButtonPressed() }

		binding.loginTextView.setOnClickListener {
			activity?.supportFragmentManager?.beginTransaction()
				?.replace(R.id.auth_fragment_container, LoginFragment.newInstance())
				?.addToBackStack(null)
				?.commit()
		}

		return binding.root
	}

	private fun signUpButtonPressed() {
		val fullname = binding.registerFullnameTextField.editText?.text.toString().trim()
		val email = binding.registerEmailTextField.editText?.text.toString().trim()
		val password = binding.registerPasswordTextField.editText?.text.toString().trim()

		if (email.isValidEmail() && password.isValidPassword() && fullname.isValidFullname()) {
			val user = UserModel(fullname, email, password, Role.PASSENGER)
			viewModel.register(user)
		} else {
			Toast.makeText(context, "One or more inputs is invalid", Toast.LENGTH_SHORT).show()
		}

	}

	companion object {
		@JvmStatic
		fun newInstance() = RegisterFragment()
	}
}