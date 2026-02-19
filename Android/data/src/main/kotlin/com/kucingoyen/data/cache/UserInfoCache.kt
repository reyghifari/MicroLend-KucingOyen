package com.kucingoyen.data.cache

interface UserInfoCache : BaseCache {

    /**
     * Emit whenever [isLoggedIn] value changed
     */
    var isLoggedInBefore: Boolean
    var email: String
    var partyId: String
    var level: Int
}
