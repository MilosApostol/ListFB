package com.example.listfirebase.data.room.addlist

import com.example.listfirebase.data.firebasedata.listfirebase.ListEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListRoomRepository @Inject constructor(val dao: ListDao) {

    suspend fun insertList(list: ListEntity){
        dao.insertList(list)
    }

    suspend fun updateList(list: ListEntity){
        dao.updateList(list)
    }

    suspend fun deleteList(list: ListEntity){
        dao.deleteList(list)
    }

     fun deleteAllLists(){
        dao.deleteAllLists()
    }

     fun getListsByUserId(userId: String): Flow<List<ListEntity>> {
        return dao.getListsByUserId(userId)
    }

    fun getLists(): Flow<List<ListEntity>>{
        return dao.getAll()
    }


}