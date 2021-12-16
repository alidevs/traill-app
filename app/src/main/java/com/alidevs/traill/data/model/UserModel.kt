package com.alidevs.traill.data.model

import com.alidevs.traill.utils.Role

data class UserModel(
	val fullname: String,
	val email: String,
	val password: String,
	val role: Role
)
