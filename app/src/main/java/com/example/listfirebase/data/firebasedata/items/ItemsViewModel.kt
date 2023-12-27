package com.example.listfirebase.data.firebasedata.items

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ItemsViewModel @Inject constructor(
    val repository: ItemsRepository
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

    fun updateData(reference: DatabaseReference, items: ItemsEntity, key: String){
        viewModelScope.launch {
            repository.updateItem(reference, items, key)
        }
    }
}