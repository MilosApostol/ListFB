package com.example.listfirebase.data.firebasedata.additemsapi

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import com.example.listfirebase.data.firebasedata.items.ItemsEntity
import com.example.listfirebase.data.retrofit.ApiService
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject


class AddItemsRepository @Inject constructor(
    private val apiService: ApiService,
    val dao: AddItemsDao
) {


    fun getAllItems(): Flow<List<AddItemsData>> {
        return dao.getAllItems()
    }
    suspend fun getItems() = withContext(Dispatchers.IO) {
        val itemsList = apiService.getItems()
        dao.insertItems(itemsList)
    }

    suspend fun getItem(title: String): AddItemsData {
        return apiService.getItem(title)
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
