package com.alidevs.traill.ui.auth

interface AuthListener {
	fun onStarted()
	fun onSuccess()
	fun onFailure(message: String)
}