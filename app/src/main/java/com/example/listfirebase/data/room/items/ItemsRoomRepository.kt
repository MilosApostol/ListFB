package com.example.listfirebase.data.room.items

import com.example.listfirebase.data.firebasedata.items.ItemsEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ItemsRoomRepository @Inject constructor(val dao: ItemsDao) {

    suspend fun insertItem(item: ItemsEntity) {
        dao.insertItem(item)
    }

    suspend fun updateItem(item: ItemsEntity) {
        dao.updateItems(item)
    }

    suspend fun deleteItems(itemsEntity: ItemsEntity) {
        dao.deleteItems(itemsEntity)
    }

    fun deleteAllItems() {
        dao.deleteAllItems()
    }

    fun getItems(): Flow<List<ItemsEntity>>{
        return dao.getAll()
    }

}