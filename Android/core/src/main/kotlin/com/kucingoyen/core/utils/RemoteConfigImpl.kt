package com.kucingoyen.core.utils

import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.gson.Gson
import com.kucingoyen.core.model.DeviceInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class RemoteConfigImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : RemoteConfig {

    private lateinit var remoteConfigFlow: MutableStateFlow<FirebaseRemoteConfig>

    private val default
        get() = hashMapOf<String, Any>(
            FORCE_UPDATE_VERSION to 1,
        )



    private val remoteConfigValue: Flow<FirebaseRemoteConfig>
        get() = callbackFlow {
            val task = remoteConfigFlow.asStateFlow().value.fetchAndActivate()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("RemoteConfig", "Fetch and Activate successful")
                        trySendBlocking(remoteConfigFlow.value)
                    } else {
                        Log.e("RemoteConfig", "Fetch failed", task.exception)
                    }
                }
            awaitClose {
                task.isCanceled
            }
        }

    override fun initialize() {
        val app = FirebaseApp.initializeApp(context) ?: Firebase.app
        val remoteConfig = Firebase.remoteConfig(app).apply {
            setConfigSettingsAsync(remoteConfigSettings {
                minimumFetchIntervalInSeconds = MINIMUM_FETCH_INTERVAL
            })
            setDefaultsAsync(default)

            reset().addOnCompleteListener {
                fetchAndActivate()
            }
        }

        remoteConfigFlow = MutableStateFlow(remoteConfig)
    }

    override suspend fun getSHA(): Flow<String> = flow {
        remoteConfigValue.collect {
            val shaValue = it.getString(FEATURE_GET_SHA).trim()
            emit(shaValue)
        }
    }

    override suspend fun getDeviceInfo(): Flow<DeviceInfo> = flow {
        remoteConfigValue.collect {
            val jsonObject = it.getString(FEATURE_DEVICE_INFO).trim()
            try {
                val deviceInfo = Gson().fromJson(jsonObject, DeviceInfo::class.java)
                emit(deviceInfo)
            } catch (e: Exception) {
                Log.e("RemoteConfig", "Error parsing JSON: ${e.message}")
            }

        }
    }


    private companion object {
        const val MINIMUM_FETCH_INTERVAL = 0L

        const val FEATURE_GET_SHA = "FEATURE_GET_SHA"

        const val FORCE_UPDATE_VERSION = "FORCE_UPDATE_VERSION"

        const val FEATURE_DEVICE_INFO = "FEATURE_DEVICE_INFO"

    }

}