package com.example.listfirebase.data.firebasedata.additemsapi

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listfirebase.data.firebasedata.items.ItemsEntity
import com.example.listfirebase.data.retrofit.ApiItemsClient.apiService
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddItemsViewModel @Inject constructor(
    private val repository: AddItemsRepository,
) : ViewModel() {

    private val searchText = MutableStateFlow("")

    private var selectedItem = mutableStateListOf<AddItemsData?>(null)

    private val _addItem = MutableStateFlow(listOf<AddItemsData>())

    val allItems: Flow<List<AddItemsData>> = repository.getAllItems()


     fun fetchItems() {
        viewModelScope.launch {
            repository.getItems()
        }
    }

    val addItem = searchText
        .combine(_addItem) { text, addItem ->
            if (text.isBlank()) {
                addItem
            } else {
                addItem.filter { item ->
                    item.title.contains(text, ignoreCase = true)
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _addItem.value
        )
    private fun fetchItemsList() {
        viewModelScope.launch {
            repository.getItems()
        }
    }

    fun getItem(title: String) {
        viewModelScope.launch {
            _addItem.value = listOf(repository.getItem(title))
        }
    }

    fun addToSelectedItems(items: AddItemsData) {
        viewModelScope.launch(Dispatchers.IO) {
            val existingItem = selectedItem.find { it?.title == items.title }
            existingItem?.let {
                it.title = items.title
                it.description = items.description
                it.price = items.price
                it.listCreatorId = items.listCreatorId
            } ?: run {
                selectedItem.addAll(listOf(items))
            }
        }
    }

    fun onSearchChange(text: String) {
        searchText.value = text
    }

    fun saveItems(ref: DatabaseReference, items: ItemsEntity, key: String) {
        viewModelScope.launch {
            repository.saveItems(ref, items, key)
        }

    }
}