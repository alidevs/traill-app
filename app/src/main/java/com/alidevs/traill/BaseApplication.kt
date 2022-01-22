package com.alidevs.traill

import android.app.Application
import com.alidevs.traill.utils.helper.PrefsHelper
import com.yariksoffice.lingver.Lingver


class BaseApplication : Application() {
	
	override fun onCreate() {
		super.onCreate()
		
		PrefsHelper.init(this)
		Lingver.init(this, "en")
	}
	
}