package com.example.listfirebase.data.room.loginregister

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listfirebase.session.UserSessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(
    val userRepository: UserRepository,
    val userSessionManager: UserSessionManager,
) : ViewModel() {

    //userSession not working well
    var userIdState by mutableStateOf<String?>(null)


    suspend fun loggingState(): Boolean {
        return withContext(Dispatchers.IO) {
            val loggedInUser = userRepository.getUserByLoggedInStatus()
            if (loggedInUser != null) {
                userSessionManager.currentUser = loggedInUser
                userSessionManager.isUserLoggedIn.value = true
                true
            } else {
                false
            }
        }
    }

    suspend fun getUserDetails(): UserEntity? {
        return userRepository.getUserByLoggedInStatus()
    }


    suspend fun getUserByUserNameAndPass(userEmail: String, userPassword: String): Boolean {
        return withContext(Dispatchers.IO) {
            val user = userRepository.getUserByName(userEmail)
            if (user != null) {
                if (user.userPassword == userPassword) {
                    userSessionManager.currentUser = user
                    userSessionManager.isUserLoggedIn.value = true
                    userRepository.updateUser(user.copy(isLoggedIn = true))  // Update in the database
                    true
                } else {
                    false
                }
            } else {
                false
            }
        }
    }


    fun getUserId() = viewModelScope.launch {
        val userId = withContext(Dispatchers.IO) {
            userRepository.getUserByLoggedInStatus()?.userId
        }
        userIdState = userId
    }


    suspend fun userExist(email: String): Boolean {
        return userRepository.userExist(email)
    }

    private suspend fun getUserByUsername(userName: String): UserEntity {
        return userRepository.getUserByName(userName)
    }

    suspend fun insertUser(user: UserEntity) {
        userRepository.insertUser(user)
        userSessionManager.apply {
            setUserLoggedIn(true)
            userSessionManager.currentUser = user
        }
    }

    suspend fun logout() {
            val loggedInUser = userRepository.getUserByLoggedInStatus()
            userRepository.updateUser(loggedInUser?.copy(isLoggedIn = false)!!)
            userSessionManager.clearSession()
    }

    suspend fun updateHolderId(username: String): Boolean {
        return userRepository.updateRoomUserIdAfterLogin(username)
    }

    suspend fun updateRoomUserIdAfterLogin(username: String): Boolean {
        return userRepository.updateRoomUserIdAfterLogin(username)
    }
}