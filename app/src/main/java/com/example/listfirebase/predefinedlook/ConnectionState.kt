package com.example.listfirebase.predefinedlook

sealed class ConnectionState {
    object Available: ConnectionState()
    object Unavailable: ConnectionState()
}