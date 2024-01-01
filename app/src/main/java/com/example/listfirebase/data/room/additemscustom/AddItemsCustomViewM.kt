package com.example.listfirebase.data.room.additemscustom

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listfirebase.data.firebasedata.additemsapi.AddItemsData
import com.example.listfirebase.data.firebasedata.additemsapi.AddItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class AddItemsCustomViewM @Inject
constructor(val repository: AddItemsCustomRep)
    : ViewModel() {

        private var _selectedItem = mutableStateListOf<AddItemsData>()
    val selectedItem: List<AddItemsData> get() = _selectedItem

    fun addItem(item: AddItemsData){
        viewModelScope.launch {
            repository.insertCustomItem(item)
        }
    }

    lateinit var gettCustomItems: Flow<List<AddItemsData>>

    init {
        viewModelScope.launch {
            gettCustomItems = repository.getItems()
        }
    }

    fun addToSelectedItems(items: AddItemsData) {
        viewModelScope.launch(Dispatchers.IO) {
            val existingItem = selectedItem.find { it.title == items.title }
            existingItem?.let {
                it.title = items.title
                it.description = items.description
                it.price = items.price
                it.id = items.id
            } ?: run {
                _selectedItem.addAll(listOf(items))
            }
        }
    }

    fun removeItem(item: AddItemsData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItem(item)
        }
    }
}