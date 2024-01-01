package com.example.listfirebase.data.room.additems

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listfirebase.data.firebasedata.items.ItemsEntity
import com.example.listfirebase.session.ListSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ItemsRoomViewModel @Inject constructor(
    val itemsRoomRepository: ItemsRoomRepository,
    val listSession: ListSession
) : ViewModel() {

    val _getAllItems = MutableStateFlow<List<ItemsEntity>>(emptyList())
    val getAllItems: StateFlow<List<ItemsEntity>> get() = _getAllItems.asStateFlow()

    init {
        viewModelScope.launch {
            itemsRoomRepository.getItems().collect {
                _getAllItems.value = it
            }
        }
    }

    suspend fun insertItemsOffline(itemsEntity: ItemsEntity): Boolean {
        return withContext(Dispatchers.IO) {
            val listId = listSession.getListId()
            run {
                itemsRoomRepository.insertItem(itemsEntity)
                itemsRoomRepository.updateItem(itemsEntity.copy(itemCreatorId = listId))
                true
            }
        }
    }

    suspend fun insertItemsOnline(itemsEntity: ItemsEntity): Boolean {
        return withContext(Dispatchers.IO) {
            itemsRoomRepository.insertItem(itemsEntity)
            true
        }
    }

    fun removeItem(itemsEntity: ItemsEntity) {
        viewModelScope.launch {
            itemsRoomRepository.deleteItems(itemsEntity)
        }
    }

    fun removeAllItems(){
        viewModelScope.launch{
            itemsRoomRepository.deleteAllItems()
        }
    }

}