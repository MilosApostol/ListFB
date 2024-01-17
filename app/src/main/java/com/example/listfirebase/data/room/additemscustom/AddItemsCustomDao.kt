package com.example.listfirebase.data.room.additemscustom

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.listfirebase.data.AppDatabase
import com.example.listfirebase.data.firebasedata.additemsapi.AddItemsData
import kotlinx.coroutines.flow.Flow

@Dao
abstract class AddItemsCustomDao() {

    @Query("SELECT * FROM `add_items_table`")
    abstract fun getAll(): Flow<List<AddItemsEntity>>


    @Query("SELECT * FROM `add_items_table` WHERE id = :id")
    abstract fun getItemById(id: String): Flow<AddItemsEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertItem(item: AddItemsEntity)

    @Delete
    abstract suspend fun deleteItem(item: AddItemsEntity)
}