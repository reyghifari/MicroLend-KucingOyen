package com.kucingoyen.core.utils

import com.kucingoyen.core.model.DeviceInfo
import kotlinx.coroutines.flow.Flow

interface RemoteConfig {

    fun initialize()

    suspend fun getSHA(): Flow<String>

    suspend fun getDeviceInfo() : Flow<DeviceInfo>

}