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
abstract class AddItemsCustomDao(database: AppDatabase) {

    @Query("SELECT * FROM `add_items_table`")
    abstract fun getAll(): Flow<List<AddItemsData>>


    @Query("SELECT * FROM `add_items_table` WHERE id = :id")
    abstract fun getItemById(id: String): Flow<AddItemsData>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertItem(item: AddItemsData)

    @Delete
    abstract suspend fun deleteItem(item: AddItemsData)
}