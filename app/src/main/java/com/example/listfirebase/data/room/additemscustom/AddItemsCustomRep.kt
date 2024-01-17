package com.example.listfirebase.data.room.additemscustom

import com.example.listfirebase.data.firebasedata.additemsapi.AddItemsData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class AddItemsCustomRep @Inject constructor(val dao: AddItemsCustomDao) {

    fun getItems(): Flow<List<AddItemsEntity>> {
        return dao.getAll()
    }

    fun getItemsById(id: String): Flow<AddItemsEntity> {
        return dao.getItemById(id)
    }

    suspend fun insertCustomItem(item: AddItemsEntity) {
        dao.insertItem(item)
    }

    suspend fun deleteItem(item: AddItemsEntity) {
        dao.deleteItem(item)
    }

}
