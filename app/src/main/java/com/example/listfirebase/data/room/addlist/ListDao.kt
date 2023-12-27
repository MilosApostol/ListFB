package com.example.listfirebase.data.room.addlist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.listfirebase.data.firebasedata.listfirebase.ListEntity
import kotlinx.coroutines.flow.Flow


@Dao
abstract class ListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertList(list: ListEntity)

    @Update
    abstract suspend fun updateList(list: ListEntity)

    @Delete
    abstract suspend fun deleteList(list: ListEntity)

    @Query("SELECT * FROM `list_table`")
    abstract fun getAll(): Flow<List<ListEntity>>

    @Query("SELECT * FROM `list_table` WHERE id = :listId")
    abstract fun getItemById(listId: String): Flow<List<ListEntity>>

    @Query("DELETE FROM `list_table`")
    abstract fun deleteAllLists()

    @Query("SELECT * FROM `list_table` WHERE listCreatorId = :userId")
    abstract fun getListsByUserId(userId: String): Flow<List<ListEntity>>

}