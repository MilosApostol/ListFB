package com.example.listfirebase.data.firebasedata.additemsapi

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "add_items_table")
data class AddItemsData(
    @PrimaryKey(autoGenerate = false)
    var id: String,
    @SerializedName("title")
    var title: String = "",
    @SerializedName("description")
    var description: String = "",
    @SerializedName("price")
    var price: String = ""
)