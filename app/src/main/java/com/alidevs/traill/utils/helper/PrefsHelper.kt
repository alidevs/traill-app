package com.alidevs.traill.utils.helper

import android.content.Context
import android.content.SharedPreferences

object PrefsHelper {
	
	private lateinit var prefs: SharedPreferences
	private const val PREFS_NAME = "TraillPrefs"
	
	fun init(context: Context) {
		prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
	}
	
	fun read(key: String, value: String): String? = prefs.getString(key, value)
	
	fun read(key: String, value: Long): Long = prefs.getLong(key, value)
	
	fun write(key: String, value: String) {
		val prefsEditor: SharedPreferences.Editor = prefs.edit()
		with(prefsEditor) {
			putString(key, value)
			commit()
		}
	}
	
	fun write(key: String, value: Long) {
		val prefsEditor: SharedPreferences.Editor = prefs.edit()
		with(prefsEditor) {
			putLong(key, value)
			commit()
		}
	}
}