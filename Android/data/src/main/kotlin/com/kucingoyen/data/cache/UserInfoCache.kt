package com.kucingoyen.data.cache

interface UserInfoCache : BaseCache {

    /**
     * Emit whenever [isLoggedIn] value changed
     */
    var isLoggedIn: Boolean
    var email: String
    var partyId: String
    var level: Int
}
