package com.example.listfirebase.data.room.loginregister

import android.content.Context
import android.widget.Toast
import com.example.listfirebase.session.UserSessionManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository
@Inject constructor(private val userDao: LoginDao, val userSessionManager: UserSessionManager) {


    suspend fun insertUser(userEntity: UserEntity) {
        userDao.insertUser(userEntity)
    }

    suspend fun updateUser(userEntity: UserEntity) {
        userDao.updateUser(userEntity)
    }

    suspend fun getUserById(userId: String): UserEntity {
        return userDao.getUserById(userId)
    }

    suspend fun getUserByName(userEmail: String): UserEntity {
        return userDao.getUserByName(userEmail)
    }

    suspend fun logout() {
        userSessionManager.clearSession()
    }

    suspend fun userExist(email: String): Boolean {
        return userDao.checkUserExists(email).first()
    }

    suspend fun updateUserId(newUserId: String, oldUserId: String) {
        userDao.updateUserId(newUserId, oldUserId)
    }


    //works
    suspend fun updateRoomUserIdAfterLogin(username: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val firebaseUserId = FirebaseAuth.getInstance().currentUser!!.uid
                val existingUser = userDao.getUserByName(username)

                if (existingUser != null) {
                    userDao.updateUserId(existingUser.userId, firebaseUserId)
                } else {
                    userDao.insertUser(UserEntity(firebaseUserId, username))
                }

                true
            } catch (e: Exception) {
                false
            }
        }

    }
}

