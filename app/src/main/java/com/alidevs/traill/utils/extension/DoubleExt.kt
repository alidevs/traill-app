package com.alidevs.traill.utils

import java.math.RoundingMode
import java.text.DecimalFormat

// Double extension to format to 2 decimal places returns a double
fun Double.ceil(): Double {
	val df = DecimalFormat("#.##")
	df.roundingMode = RoundingMode.CEILING
	return df.format(this).toDouble()
}