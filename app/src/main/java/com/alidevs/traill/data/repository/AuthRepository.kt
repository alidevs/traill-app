package com.alidevs.traill.data.repository

import com.alidevs.traill.data.service.AuthService
import com.alidevs.traill.data.model.UserModel

class AuthRepository {

	private val authService = AuthService.getInstance()

	fun login(email: String, password: String) = authService.login(email, password)

	fun register(user: UserModel) = authService.register(user)

	fun getCurrentUser() = authService.getCurrentUser()

}