package com.alidevs.traill.utils.helper

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.alidevs.traill.R
import com.alidevs.traill.ui.main.MainActivity

object NotifsHelper {
	
	private const val CHANNEL_ID = "Traill"
	
	private fun showNotification(context: Context, title: String?, body: String?) {
		Intent(context, MainActivity::class.java).also { intent ->
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
			
			val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
				PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE)
			} else {
				PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT)
			}
			
			val notification = NotificationCompat.Builder(context, CHANNEL_ID)
				.setSmallIcon(R.drawable.ic_launcher_foreground)
				.setContentTitle(title)
				.setContentText(body)
				.setAutoCancel(true)
				.setContentIntent(pendingIntent)
			
			NotificationManagerCompat.from(context).notify(0, notification.build())
		}
	}
	
	fun sendNotificationToActivity(context: Context, title: String?, body: String?) {
		showNotification(context, title, body)
		
		val intent = Intent("com.alidevs.traill.notification")
		intent.putExtra("title", title)
		intent.putExtra("body", body)
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
	}
	
}