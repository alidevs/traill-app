package com.alidevs.traill.utils

import android.util.Patterns

fun String.isValidEmail() =
	Patterns.EMAIL_ADDRESS.matcher(this).matches() && this.isNotEmpty()

fun String.isValidPassword() = this.length >= 6

fun String.isValidFullname() = this.length >= 3 && this.contains(" ")