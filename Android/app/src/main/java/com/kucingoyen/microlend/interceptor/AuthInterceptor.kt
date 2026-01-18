package com.kucingoyen.microlend.interceptor

import com.kucingoyen.data.cache.AppSessionCache
import com.kucingoyen.data.utils.ApiConst
import com.kucingoyen.data.utils.ApiConst.HEADER_ACCEPT_VALUE
import com.kucingoyen.data.utils.ApiConst.HEADER_NO_AUTH
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

internal class AuthInterceptor @Inject constructor(
    private val appSessionCache: AppSessionCache
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.addHeader(ApiConst.HEADER_ACCEPT, HEADER_ACCEPT_VALUE)

        if (chain.request().headers[HEADER_NO_AUTH] == null) {
            appSessionCache.token.takeIf { it.isNotEmpty() }?.let { token ->
                builder.addHeader(ApiConst.HEADER_AUTHORIZATION, ApiConst.getBearer(token))
            }
        } else {
            builder.removeHeader(HEADER_NO_AUTH)
        }

        return chain.proceed(builder.build())
    }

}
