package com.alidevs.traill.data.repository

import com.alidevs.traill.data.firebase.FirebaseService

class AuthRepository {

	private val firebaseService = FirebaseService.getInstance()

	fun login(email: String, password: String) = firebaseService.login(email, password)

	fun register(email: String, password: String) = firebaseService.register(email, password)

	fun logout() = firebaseService.logout()

	fun getCurrentUser() = firebaseService.getCurrentUser()

}