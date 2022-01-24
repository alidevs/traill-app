package com.alidevs.traill.utils.service

import android.util.Log
import com.alidevs.traill.utils.helper.NotifsHelper
import com.alidevs.traill.utils.helper.PrefsHelper
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.reactivex.Single

class FirebaseInstanceService : FirebaseMessagingService() {
	
	override fun onCreate() {
		super.onCreate()
		
		Log.d("FirebaseInstanceService", "onCreate")
	}
	
	override fun onNewToken(token: String) {
		super.onNewToken(token)
		
		PrefsHelper.write("FCM_TOKEN", token)
		Log.i("FirebaseInstanceService", "onNewToken: $token")
	}
	
	override fun onMessageReceived(message: RemoteMessage) {
		super.onMessageReceived(message)
		
		if (message.notification != null) {
			val body = message.notification?.body
			val title = message.notification?.title
			
			NotifsHelper.sendNotificationToActivity(this, title, body)
		}
	}
	
	companion object {
		fun getToken() = Single.create<String> { emitter ->
			FirebaseMessaging
				.getInstance()
				.token
				.addOnSuccessListener { emitter.onSuccess(it) }
				.addOnFailureListener { emitter.onError(it) }
		}
	}
	
}