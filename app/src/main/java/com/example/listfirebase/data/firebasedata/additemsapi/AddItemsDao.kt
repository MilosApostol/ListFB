package com.example.listfirebase.data.firebasedata.additemsapi

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AddItemsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItems(itemsList: List<AddItemsData>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(item: AddItemsData)
    @Query("SELECT * FROM `add_items_table` ORDER BY price DESC")
    fun getAllItems(): Flow<List<AddItemsData>>
}