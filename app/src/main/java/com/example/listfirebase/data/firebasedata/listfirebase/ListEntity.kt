package com.example.listfirebase.data.firebasedata.listfirebase

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "list_table")
data class ListEntity(
    @PrimaryKey(autoGenerate = false)
    var id: String = "tururur",
    val listName: String = "",
    val listCreatorId: String = ""
)
