package com.example.listfirebase.data.firebasedata.items

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "item_table")
data class ItemsEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "itemId")
    var itemId: String = "",
    @ColumnInfo(name = "listID")
    var itemCreatorId: String = "",
    var itemName: String = "",
    var description: String = "",
    var sync: String = "0"
)