package com.example.listfirebase.data.firebasedata.registerlogin

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.room.util.copy
import com.example.listfirebase.data.room.loginregister.UserEntity
import com.example.listfirebase.data.room.loginregister.UserRepository
import com.example.listfirebase.nav.Screens
import com.example.listfirebase.session.UserSessionManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    val connectivityManager: ConnectivityManager,
    val repository: RegisterRepository,
    val userRepository: UserRepository,
    val auth: FirebaseAuth,
    val userSessionManager: UserSessionManager
) : ViewModel() {
    private val _isUserLoggedInState = MutableStateFlow(false)
    val isUserLoggedInState = _isUserLoggedInState.asStateFlow()
    fun isNetworkAvailable(): Boolean {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
    }

    private suspend fun getUserByUsername(userName: String): UserEntity {
        return userRepository.getUserByName(userName)
    }
//update I  dont think this works
    // if the user is logged, if yes set userID to firebaseUserId , then you can find your user in ROOM

    fun isUserLoggedIn(): Boolean {
        return if (auth.currentUser != null) {
            userSessionManager.setUserId(Firebase.auth.currentUser!!.uid)
            true
        } else {
            false
        }
    }

    suspend fun onSignUp(
        email: String,
        password: String,
        key: String
    ) {
        repository.signUp(email, password, key)
        val user = UserEntity(
            userEmail = email,
            userPassword = password,
            userHolderId = key,
            isLoggedIn = true
        )
        userRepository.insertUser(
            user
        )
        Firebase.auth.currentUser?.uid?.let { userSessionManager.setUserId(it) }
        userSessionManager.apply {
            setUserLoggedIn(true)
            _isUserLoggedInState.value = true
            userSessionManager.currentUser = user
        }
    }

    suspend fun logInAfterOffline(email: String, password: String) {
        _isUserLoggedInState.value = true
        repository.logIn(email, password)
    }

    fun getUserId(): String {
        return userSessionManager.getUserId()
    }

    suspend fun logIn(email: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            if (isNetworkAvailable()) {
                val user = userRepository.getUserByName(email)
                if (user != null) {
                    if (user.userPassword == password) {
                        userSessionManager.currentUser = user
                        _isUserLoggedInState.value = true
                        userRepository.updateUser(user.copy(isLoggedIn = true))
                        return@withContext repository.logIn(email, password)
                    } else {
                        return@withContext false
                    }
                } else {
                    return@withContext false
                }
            }
            return@withContext false
        }
    }
    fun signOut() {
        auth.signOut()
        _isUserLoggedInState.value = false

    }
}