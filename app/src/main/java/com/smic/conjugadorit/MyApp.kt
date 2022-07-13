package com.smic.conjugadorit

import android.app.Application
import android.content.Context
import com.smic.conjugadorit.di.AppComponent
import com.smic.conjugadorit.di.DaggerAppComponent


/**
 * @author Smogevscih Yuri
08.03.2022
 **/
class MyApp : Application() {

    companion object {
        lateinit var appContext: Context
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        appComponent = DaggerAppComponent.create()
    }
}