package com.kucingoyen.data.cache.impl

import android.content.SharedPreferences
import com.kucingoyen.data.cache.AppSessionCache
import com.kucingoyen.data.cache.AppSessionCache.Companion.LIFETIME_INFINITY
import com.kucingoyen.data.utils.extentions.remove
import com.kucingoyen.data.utils.extentions.update
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

internal class AppSessionCacheImpl @Inject constructor(
    private val pref: SharedPreferences
) : AppSessionCache {

    private val changeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->

    }

    init {
        pref.registerOnSharedPreferenceChangeListener(changeListener)
    }

    private val _tokenFlow = MutableStateFlow("")
    override val tokenFlow: Flow<String> = _tokenFlow

    override var token: String
        get() = pref.getString(TOKEN, null).orEmpty()
        set(value) {
            pref.update(value to TOKEN)
        }

    override var refreshToken: String
        get() = pref.getString(REFRESH_TOKEN, null).orEmpty()
        set(value) {
            pref.update(value to REFRESH_TOKEN)
        }

    override var lifeTime: Long
        get() = pref.getLong(LIFE_TIME, LIFETIME_INFINITY)
        set(value) {
            pref.update(value to LIFE_TIME)
        }

    override fun invalidate() {
        pref.remove(
            TOKEN,
            REFRESH_TOKEN,
            LIFE_TIME
        )
    }

    private companion object {
        const val TOKEN = "AppSessionCacheImpl.TOKEN"
        const val REFRESH_TOKEN = "AppSessionCacheImpl.REFRESH_TOKEN"
        const val LIFE_TIME = "AppSessionCacheImpl.LIFE_TIME"
    }

}
