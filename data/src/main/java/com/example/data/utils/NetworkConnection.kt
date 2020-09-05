package com.example.data.utils

import android.content.Context
import android.net.ConnectivityManager

class NetworkConnection(private val context: Context)
{
        fun isOnline(): Boolean
        {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo

            if (activeNetwork != null)
                return activeNetwork.type == ConnectivityManager.TYPE_WIFI ||
                        activeNetwork.type == ConnectivityManager.TYPE_MOBILE

            return false
        }
}