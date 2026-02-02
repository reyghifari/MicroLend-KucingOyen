package com.kucingoyen.microlend

import android.app.Application
import com.kucingoyen.core.utils.RemoteConfig
import com.kucingoyen.data.cache.AppSessionCache
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication: Application() {

    @Inject
    lateinit var remoteConfig: RemoteConfig

    @Inject
    lateinit var appSessionCache: AppSessionCache

    override fun onCreate() {
        super.onCreate()

    }

}
