package com.alidevs.traill.ui.view.auth

interface AuthListener {
	fun onStarted()
	fun onSuccess()
	fun onFailure(message: String)
}