package com.example.listfirebase.data.room.loginregister

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.listfirebase.session.UserSessionManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(
    val userRepository: UserRepository,
    private val userSessionManager: UserSessionManager,
) : ViewModel() {

    suspend fun getUserByUserNameAndPass(userEmail: String, userPassword: String): Boolean {
        return withContext(Dispatchers.IO) {
            val user = userRepository.getUserByName(userEmail)
            userSessionManager.setUser(user = user)
            user.let {
                return@withContext it.userPassword == userPassword
            }
        }
    }

    suspend fun userExist(email: String): Boolean {
        return userRepository.userExist(email)
    }

    private suspend fun getUserByUsername(userName: String): UserEntity {
        return userRepository.getUserByName(userName)
    }

    //not working
/*
    fun user(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userSessionManager.getUser()
            val newId = Firebase.auth.currentUser?.uid
            val oldId = user.userId ?: ""
            userRepository.updateUserId(oldId, newId!!)
        }
    }


 */

    suspend fun insertUser(user: UserEntity) {
        userRepository.insertUser(user)
        userSessionManager.apply {
            setUserLoggedIn(true)
            setUser(user)
        }
    }


    fun getUserById(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val savedUser = userRepository.getUserById(userId)
            userSessionManager.setUser(savedUser)
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

    suspend fun updateRoomUserIdAfterLogin(username: String): Boolean {
        return userRepository.updateRoomUserIdAfterLogin(username)
    }
}