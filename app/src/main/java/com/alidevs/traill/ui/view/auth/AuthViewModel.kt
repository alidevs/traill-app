package com.alidevs.traill.ui.view.auth

import androidx.lifecycle.ViewModel
import com.alidevs.traill.data.model.UserModel
import com.alidevs.traill.data.repository.AuthRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AuthViewModel : ViewModel() {

	private val repository = AuthRepository()

	val user by lazy { repository.getCurrentUser() }

	var authListener: AuthListener? = null

	private val disposables = CompositeDisposable()

	fun login(email: String, password: String) {
		if (email.isEmpty() || password.isEmpty()) {
			authListener?.onFailure("Invalid email or password")
			return
		}

		// Authentication started
		authListener?.onStarted()

		// Login
		val disposable = repository.login(email, password)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe({
				authListener?.onSuccess()
			}, {
				authListener?.onFailure(it.message!!)
			})

		disposables.add(disposable)
	}

	fun register(user: UserModel) {
		if (user.email.isEmpty() || user.password.isEmpty()) {
			authListener?.onFailure("Invalid email or password")
			return
		}

		// Authentication started
		authListener?.onStarted()

		// Register
		val disposable = repository.register(user)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe({
				authListener?.onSuccess()
			}, {
				authListener?.onFailure(it.message!!)
			})

		disposables.add(disposable)
	}

	override fun onCleared() {
		super.onCleared()
		disposables.dispose()
	}
}