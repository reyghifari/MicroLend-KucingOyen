package com.kucingoyen.data.cache

import kotlinx.coroutines.flow.Flow

interface AppSessionCache : BaseCache {
    /**
     * Emit whenever [token] value changed
     */
    val tokenFlow: Flow<String>

    var token: String
    var refreshToken: String
    var lifeTime: Long

    companion object {
        const val LIFETIME_INFINITY = -1L
    }

}
