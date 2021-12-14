package com.alidevs.traill.data.repository

import com.alidevs.traill.data.firebase.FirebaseService
import com.alidevs.traill.data.model.UserModel

class AuthRepository {

	private val firebaseService = FirebaseService.getInstance()

	fun login(email: String, password: String) = firebaseService.login(email, password)

	fun register(user: UserModel) = firebaseService.register(user)

	fun getCurrentUser() = firebaseService.getCurrentUser()

}