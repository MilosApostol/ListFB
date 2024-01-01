package com.example.listfirebase.data.room.addlist

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listfirebase.data.firebasedata.listfirebase.ListEntity
import com.example.listfirebase.data.room.loginregister.UserRepository
import com.example.listfirebase.data.room.loginregister.UserViewModel
import com.example.listfirebase.session.ListSession
import com.example.listfirebase.session.UserSessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListRoomViewModel @Inject constructor(
    val repository: ListRoomRepository,
    val userSessionManager: UserSessionManager,
    val listSession: ListSession,
    val userRepository: UserRepository
) : ViewModel() {

    val expandedStates = mutableStateMapOf<String, Boolean>()

     val _activeState = MutableStateFlow(false)
    val activeState = _activeState.asStateFlow()

    private val _getAllLists = MutableStateFlow<List<ListEntity>>(emptyList())
    val getAllLists: StateFlow<List<ListEntity>> get() = _getAllLists.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getLists().collect {
                _getAllLists.value = it
            }
        }
    }

    fun getListById(listId: String): Flow<List<ListEntity>> {
        return repository.getListsByUserId(listId)
    }

    suspend fun insertListOffline(list: ListEntity): Boolean {
        return withContext(Dispatchers.IO) {
            val user = userRepository.getUserByLoggedInStatus()
            if (user != null) {
                repository.insertList(list)
                repository.updateList(list.copy(listCreatorId = user.userId))
                true
            } else {
                false
            }
        }
    }

    //returns True as long as getUserId is not null
    suspend fun insertListOnline(list: ListEntity): Boolean {
        return withContext(Dispatchers.IO) {
            repository.insertList(list)
            true
        }
    }

    fun updateList(list: ListEntity) {
        viewModelScope.launch {
            repository.updateList(list)
        }
    }

    fun removeList(list: ListEntity) {
        viewModelScope.launch {
            repository.deleteList(list)
        }
    }

    fun removeAllLists() {
        viewModelScope.launch {
            repository.deleteAllLists()
        }
    }
}