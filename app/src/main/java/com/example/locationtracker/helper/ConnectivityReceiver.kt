//package com.example.locationtracker
//
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.net.ConnectivityManager
//import android.net.NetworkCapabilities
//import android.net.NetworkInfo
//
//class ConnectivityReceiver(private val connectivityReceiverListener: ConnectivityReceiverListener) : BroadcastReceiver() {
//
//    override fun onReceive(context: Context, intent: Intent) {
//        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
//        val isConnected = activeNetwork?.isConnected == true
//        connectivityReceiverListener.onNetworkConnectionChanged(isConnected)
//    }
//
//    interface ConnectivityReceiverListener {
//        fun onNetworkConnectionChanged(isConnected: Boolean)
//    }
//
//    companion object {
//        fun isConnected(context: Context): Boolean {
//            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//            val activeNetwork = connectivityManager.activeNetworkInfo
//            return activeNetwork?.isConnected == true
//        }
//    }
//}
package com.example.locationtracker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo

class ConnectivityReceiver(private val connectivityReceiverListener: ConnectivityReceiverListener) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnected = activeNetwork?.isConnected == true
        connectivityReceiverListener.onNetworkConnectionChanged(isConnected)
    }

    interface ConnectivityReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }

    companion object {
        fun isConnected(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetworkInfo
            return activeNetwork?.isConnected == true
        }
    }
}
