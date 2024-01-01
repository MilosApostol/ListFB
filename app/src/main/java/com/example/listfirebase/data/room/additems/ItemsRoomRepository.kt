package com.example.listfirebase.data.room.additems

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

    suspend fun removeItem(itemsEntity: ItemsEntity){
        dao.deleteItems(itemsEntity)
    }

    suspend fun getItemById(itemId: String){
        dao.getItemById(itemId)
    }

    suspend fun getItemByListId(listId: String): Flow<List<ItemsEntity>> {
        return dao.getItemsByListId(listId)
    }

    fun getItems(): Flow<List<ItemsEntity>>{
        return dao.getAll()
    }

}