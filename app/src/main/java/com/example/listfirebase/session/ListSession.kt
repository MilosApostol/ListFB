package com.example.listfirebase.session

import androidx.compose.runtime.mutableStateOf
import com.example.listfirebase.data.firebasedata.listfirebase.ListEntity
import com.example.listfirebase.data.room.loginregister.UserEntity

class ListSession {
    var currentList: ListEntity? = null

    fun setUser(user: ListEntity) {
        this.currentList = user
    }

    fun getUserId(): String {
        return currentList?.id!!
    }

    fun setUserId(newUserId: String) {
        currentList?.id = newUserId
    }

    fun getUser(): ListEntity {
        return currentList!!
    }

    fun clearSession() {
        currentList = null
    }

    fun logout() {
        clearSession()
    }

}