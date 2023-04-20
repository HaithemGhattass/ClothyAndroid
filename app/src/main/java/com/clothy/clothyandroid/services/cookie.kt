package com.clothy.clothyandroid.services

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class CookieInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()

        // Read cookies from shared preferences
        val sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val cookie = sharedPreferences.getString("cookie", "")

        // Add cookies to request headers
        if (!cookie.isNullOrEmpty()) {
            builder.addHeader("Cookie", cookie)
        }

        val request = builder.build()
        return chain.proceed(request)
    }
}
