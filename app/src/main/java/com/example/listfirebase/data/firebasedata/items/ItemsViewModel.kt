package com.example.listfirebase.data.firebasedata.items

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listfirebase.data.room.additems.ItemsRoomRepository
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class ItemsViewModel @Inject constructor(
    val repository: ItemsRepository,
    val repositoryRoom: ItemsRoomRepository
) : ViewModel() {


    lateinit var getAllItems: Flow<List<ItemsEntity>>

    init {
        viewModelScope.launch {
            getAllItems = flow {
                repository.readData()
                emitAll(repository.getItemsFlow())

            }
        }
    }

    fun removeItem(itemId: String) {
        viewModelScope.launch {
            repository.removeItem(itemId)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteAll()
                repositoryRoom.deleteAllItems()
            }
        }
    }

        fun updateData(reference: DatabaseReference, items: ItemsEntity, key: String) {
            viewModelScope.launch {
                repository.updateItem(reference, items, key)
            }
        }
    }