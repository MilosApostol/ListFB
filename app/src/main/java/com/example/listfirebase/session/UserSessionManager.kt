package com.example.listfirebase.session

import android.os.Build
import android.se.omapi.Session
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import com.example.listfirebase.data.room.loginregister.UserEntity

class UserSessionManager {
    var currentUser: UserEntity? = null
    var isUserLoggedIn = mutableStateOf(
        false
    )

    fun getUserId(): String {
        return currentUser?.userId ?: ""
    }

    fun setUserId(newUserId: String){
        currentUser?.userId = newUserId
    }

    fun getUser(): UserEntity {
        return currentUser!!
    }

    fun setUserLoggedIn(loggedIn: Boolean){
        isUserLoggedIn.value = loggedIn
    }

    fun signout(){
        clearSession()
    }
     fun clearSession(){
        isUserLoggedIn.value = false
        currentUser = null
    }

}