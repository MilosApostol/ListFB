package com.example.listfirebase.utill

import android.content.Context

class ContextProvider(val context: Context) {

    fun hasInternetConnection() = context.hasInternetConnection() //can use through app state of network
}