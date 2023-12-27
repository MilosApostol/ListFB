package com.example.listfirebase.data.firebasedata.additemsapi

import androidx.compose.runtime.mutableStateListOf
import com.example.listfirebase.data.firebasedata.items.ItemsEntity
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject


class AddItemsRepository @Inject constructor(
    private val apiService: ApiService
) {
    private var _selectedItem = mutableStateListOf<ItemsEntity?>(null)
    val selectedItem: List<ItemsEntity?> get() = _selectedItem



    suspend fun getItems(): List<AddItemsData> {
        return apiService.getItems()
    }

    suspend fun getItem(title: String): AddItemsData {
        return apiService.getItem(title)
    }

    fun addToSelectedItems(items: ItemsEntity) {
        val existingItem = selectedItem.find { it?.itemName == items.itemName }
        existingItem?.let {
            it.itemName = items.itemName
            it.description = items.description
            it.itemId = it.itemId
            it.itemCreatorId = it.itemCreatorId
        } ?: run {
            _selectedItem.addAll(listOf(items))
        }
    }

    fun saveItems(ref: DatabaseReference, items: ItemsEntity, key: String) {
        ref.setValue(items).addOnCompleteListener { task ->
            items.copy(itemId = key)
            if (task.isSuccessful) {

            } else {
            }
        }
    }
}
