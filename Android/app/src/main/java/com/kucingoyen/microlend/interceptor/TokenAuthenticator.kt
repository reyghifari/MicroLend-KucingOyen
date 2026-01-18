package com.kucingoyen.microlend.interceptor

import android.content.Context
import androidx.core.net.toUri
import com.kucingoyen.data.cache.AppSessionCache
import com.kucingoyen.data.di.TagInjection
import com.kucingoyen.data.utils.ApiConst
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Named

internal class TokenAuthenticator @Inject constructor(
    @ApplicationContext private val context: Context,
    private val appSessionCache: AppSessionCache,
    @Named(TagInjection.APP_VERSION_NAME) private val appVersionName: String,
) : Authenticator {

    @Synchronized
    override fun authenticate(route: Route?, response: Response): Request? {
        //endpoint whitelist
        //remove the first "/" from path
        when (response.request.url.toString().toUri().path?.removePrefix("/")) {

        }

        val expiredToken = appSessionCache.token
        if (expiredToken.isEmpty()) {
            return null
        }

        if (response.request.header(ApiConst.HEADER_AUTHORIZATION) != null) {
            if (appSessionCache.token != expiredToken) {
                return response.buildHeader()
            }

            val tokenResponse = try {
               //hit service refresh token
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }

            with(appSessionCache) {
                token =  "TOKEN"
                refreshToken = "REFRESH_TOKEN"
                lifeTime = AppSessionCache.LIFETIME_INFINITY
            }

            //retry request with new token
            return response.buildHeader()
        }

        return null
    }

    private fun Response.buildHeader(): Request = request.newBuilder()
        .removeHeader(ApiConst.HEADER_AUTHORIZATION)
        .header(
            ApiConst.HEADER_AUTHORIZATION,
            ApiConst.getBearer(appSessionCache.token)
        ).build()

}
