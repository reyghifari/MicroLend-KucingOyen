package com.kucingoyen.data.cache.impl

import android.content.SharedPreferences
import com.kucingoyen.data.cache.UserInfoCache
import com.kucingoyen.data.utils.extentions.update
import javax.inject.Inject

internal class UserInfoCacheImpl @Inject constructor(
    private val pref: SharedPreferences,
) : UserInfoCache {

    override var email: String
        get() = pref.getString(EMAIL, "").orEmpty()
        set(value) {
            pref.update(value to EMAIL)
        }

    override var partyId: String
        get() = pref.getString(PARTY_ID, "").orEmpty()
        set(value) {
            pref.update(value to PARTY_ID)
        }

    override var level: Int
        get() = pref.getInt(LEVEL, 1)
        set(value) {
            pref.update(value to LEVEL)
        }

    override var isLoggedIn: Boolean
        get() = pref.getBoolean(IS_LOGGED_IN, false)
        set(value) {
            pref.update(value to IS_LOGGED_IN)
        }

    override fun invalidate() {

    }

    companion object {
        const val EMAIL = "UserInfoCacheImpl.EMAIL"
        const val PARTY_ID = "UserInfoCacheImpl.PARTY_ID"
        const val LEVEL = "UserInfoCacheImpl.LEVEL"
        const val IS_LOGGED_IN = "UserInfoCacheImpl.IS_LOGGED_IN"
    }




}