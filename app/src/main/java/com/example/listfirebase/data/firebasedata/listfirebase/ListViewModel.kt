package com.example.listfirebase.data.firebasedata.listfirebase

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listfirebase.data.firebasedata.items.ItemsEntity
import com.example.listfirebase.data.room.loginregister.UserEntity
import com.example.listfirebase.data.room.loginregister.UserRepository
import com.example.listfirebase.session.UserSessionManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    val repository: ListRepository,
    val connectivityManager: ConnectivityManager,
    val userSessionManager: UserSessionManager,
    val userRepository: UserRepository
) : ViewModel() {

    fun isNetworkAvailable(): Boolean {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
    }

    private val _listFlow = MutableStateFlow<List<ListEntity>>(emptyList())
    val listFlow: StateFlow<List<ListEntity>> get() = _listFlow.asStateFlow()

    private val _itemsFlow = MutableStateFlow<List<ItemsEntity>>(emptyList())
    val itemsFlow: StateFlow<List<ItemsEntity>> get() = _itemsFlow.asStateFlow()

    lateinit var getAllLists: Flow<List<ListEntity>>

    init {
        viewModelScope.launch {
            getAllLists = flow {
                repository.readData()
                emitAll(repository.getLists())
            }
        }
    }

    fun changeUserId(context: Context, authUserId: String) {
        viewModelScope.launch {
            val currentId = userSessionManager.getUserId()
            val userId = userSessionManager.setUserId(authUserId!!)
            Toast.makeText(context, "old $currentId new $userId", Toast.LENGTH_LONG).show()
        //    userRepository.updateUserId(currentId, authUserId)
        }
    }

    fun saveData(reference: DatabaseReference, list: ListEntity, key: String) {
        viewModelScope.launch {
            repository.saveData(reference, list, key)
        }
    }

    fun signOut() {
        Firebase.auth.signOut()
    }

    fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAll()
        }

    }


}



