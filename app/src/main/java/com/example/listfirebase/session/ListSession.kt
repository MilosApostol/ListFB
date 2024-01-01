package com.example.listfirebase.session

import androidx.compose.runtime.mutableStateOf
import com.example.listfirebase.data.firebasedata.listfirebase.ListEntity
import com.example.listfirebase.data.room.loginregister.UserEntity

class ListSession {
    var currentList: ListEntity? = null

    fun setList(list: ListEntity) {
        this.currentList = list
    }

    fun getListId(): String {
        return currentList?.id!!
    }

    fun getListId(newList: String) {
        currentList?.id = newList
    }

    fun getList(): ListEntity {
        return currentList!!
    }

    fun clearSession() {
        currentList = null
    }

    fun logout() {
        clearSession()
    }

}