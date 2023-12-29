package com.example.listfirebase.data.room.additems

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.listfirebase.data.firebasedata.items.ItemsEntity
import com.example.listfirebase.data.firebasedata.listfirebase.ListEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ItemsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertItem(item: ItemsEntity)

    @Update
    abstract suspend fun updateItems(item: ItemsEntity)

    @Delete
    abstract suspend fun deleteItems(item: ItemsEntity)

    @Query("SELECT * FROM `item_table` WHERE itemId = :itemId")
    abstract fun getItemById(itemId: String): Flow<List<ItemsEntity>>

    @Query("SELECT * FROM `item_table`")
    abstract fun getAll(): Flow<List<ItemsEntity>>

    @Query("DELETE FROM `item_table`")
    abstract fun deleteAllItems()

    @Query("SELECT * FROM `item_table` WHERE listID = :listId")
    abstract fun getItemsByListId(listId: String): Flow<List<ItemsEntity>>

}