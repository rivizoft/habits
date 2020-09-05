package com.example.data.server

import com.example.domain.constants.ApiConstants
import okhttp3.Interceptor
import okhttp3.Response

class CustomInterceptor : Interceptor
{
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", ApiConstants.TOKEN)
            .build()

        return chain.proceed(request)
    }
}