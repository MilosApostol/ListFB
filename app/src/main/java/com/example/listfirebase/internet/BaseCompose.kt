package com.example.listfirebase.internet

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.NavHostController
import com.example.listfirebase.nav.Screens
import com.example.listfirebase.ui.theme.ListFirebaseTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


abstract class BaseCompose : ComponentActivity() {


    private var connectivityManager: ConnectivityManager? = null
    var connectionLost = false

    private val networkCallback: ConnectivityManager.NetworkCallback =
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                runOnUiThread {
                    if (connectionLost) {
                        onNetworkAvailable(true)
                    }
                }
            }

            override fun onLost(network: Network) {
                runOnUiThread {
                    onNetworkAvailable(false)
                    connectionLost = true
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ListFirebaseTheme {
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    initUi(savedInstanceState)
                }
            }
        }
    }
    open fun initUi(savedInstanceState: Bundle?) {}

    open fun onNetworkAvailable(available: Boolean) {}
    override fun onResume() {
        super.onResume()
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager?.registerDefaultNetworkCallback(networkCallback)
    }

    override fun onPause() {
        super.onPause()
        connectivityManager?.unregisterNetworkCallback(networkCallback)
        connectivityManager = null
    }

    override fun onDestroy() {
        super.onDestroy()
        if (connectivityManager != null) {
            connectivityManager?.unregisterNetworkCallback(networkCallback)
            connectivityManager = null
        }
    }
}

    /*
    private fun clearCredentials(sharedPreferences: SharedPreferences) {
        sharedPreferences.edit()
            .remove("email")
            .remove("password")
            .putBoolean("stayLoggedIn", false)
            .apply()
    }
}
    fun attemptSilentLogin(sharedPreferences: SharedPreferences) {
        val email = sharedPreferences.getString("email", null)
        val password = sharedPreferences.getString("password", null)
        val stayLoggedIn = sharedPreferences.getBoolean("stayLoggedIn", false)

        if (email != null && password != null && stayLoggedIn) {
            Firebase.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Silent login successful
                    } else {
                        // Silent login failed, clear credentials and prompt for login
                        clearCredentials(sharedPreferences)
                    }
                }
        }
    }

 */
