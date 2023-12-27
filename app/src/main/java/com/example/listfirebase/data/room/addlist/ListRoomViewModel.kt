package com.example.listfirebase.data.room.addlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listfirebase.data.firebasedata.listfirebase.ListEntity
import com.example.listfirebase.session.ListSession
import com.example.listfirebase.session.UserSessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListRoomViewModel @Inject constructor(
    val repository: ListRoomRepository,
   val userSessionManager: UserSessionManager,
    val listSession: ListSession
) : ViewModel() {


    lateinit var getAllLists: Flow<List<ListEntity>>
    init {
        viewModelScope.launch() {
            getAllLists = repository.getLists()
        }
    }

    /*
fun fetchUserLists(userId: String): Flow<List<ListEntity>> {
    viewModelScope.launch {
        repository.getListsByUserId(userId)
    }
}

*/
    fun getListById(listId: String): Flow<List<ListEntity>> {
        return repository.getListsByUserId(listId)
    }

    fun insertList(list: ListEntity){
        viewModelScope.launch {
           repository.insertList(list)
         // val userId = userSessionManager.getUserId()
         //  listSession.setUserId(userId)
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