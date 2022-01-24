package com.alidevs.traill

import android.app.Application
import android.content.Intent
import android.util.Log
import com.alidevs.traill.utils.helper.PrefsHelper
import com.alidevs.traill.utils.service.FirebaseInstanceService
import com.yariksoffice.lingver.Lingver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subscribers.DisposableSubscriber


class BaseApplication : Application() {
	
	private val disposables = CompositeDisposable()
	
	override fun onCreate() {
		super.onCreate()
		
		PrefsHelper.init(this)
		Lingver.init(this, "en")
		Intent(this, FirebaseInstanceService::class.java).also {
			startService(it)
		}
		
		val fcmToken = PrefsHelper.read("FCM_TOKEN", "")
		
		if (fcmToken.isNullOrEmpty()) {
			FirebaseInstanceService
				.getToken()
				.subscribe({ token ->
					PrefsHelper.write("FCM_TOKEN", token)
					Log.i("BaseApplication", "onCreate: $token")
				}, { error ->
					Log.e("BaseApplication", "onCreate: ${error.message}", error)
				}).also { disposables.add(it) }
		} else {
			Log.i("BaseApplication", "Prefs: onCreate: $fcmToken")
		}
		
	}
	
}