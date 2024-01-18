package com.example.listfirebase.data.room.additemscustom

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listfirebase.data.firebasedata.additemsapi.AddItemsData
import com.example.listfirebase.data.firebasedata.additemsapi.AddItemsRepository
import com.example.listfirebase.data.firebasedata.items.ItemsEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddItemsCustomViewM @Inject
constructor(val repository: AddItemsCustomRep) : ViewModel() {

    private var selectedItem = mutableStateListOf<AddItemsEntity>()
    private val searchText = MutableStateFlow("")

    fun addItem(item: AddItemsEntity) {
        viewModelScope.launch {
            repository.insertCustomItem(item)
        }
    }

    val getCustomItems: Flow<List<AddItemsEntity>> = repository.getItems()

    fun addToSelectedItems(items: AddItemsEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val existingItem = selectedItem.find { it.title == items.title }
            existingItem?.let {
                it.title = items.title
                it.description = items.description
                it.price = items.price
                it.id = items.id
            } ?: run {
                selectedItem.addAll(listOf(items))
            }
        }
    }

    fun onSearchChange(text: String) {
        searchText.value = text
    }

    fun removeItem(item: AddItemsEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItem(item)
        }
    }
}