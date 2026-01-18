package com.kucingoyen.microlend

import android.app.Application
import com.kucingoyen.core.utils.RemoteConfig
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication: Application() {

    @Inject
    lateinit var remoteConfig: RemoteConfig

    override fun onCreate() {
        super.onCreate()

//        remoteConfig.initialize()
    }

}
