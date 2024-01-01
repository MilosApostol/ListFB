package com.example.listfirebase.data.room.additemscustom

import com.example.listfirebase.data.firebasedata.additemsapi.AddItemsData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class AddItemsCustomRep @Inject constructor(val dao: AddItemsCustomDao) {

    fun getItems(): Flow<List<AddItemsData>> {
        return dao.getAll()
    }

    fun getItemsById(id: String): Flow<AddItemsData> {
        return dao.getItemById(id)
    }

    suspend fun insertCustomItem(item: AddItemsData) {
        dao.insertItem(item)
    }

    suspend fun deleteItem(item: AddItemsData) {
        dao.deleteItem(item)
    }

}
