package com.example.listfirebase.data.room.additemscustom

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.UUID

@Entity(tableName = "add_items_table")
data class AddItemsEntity(
    @PrimaryKey(autoGenerate = false)
    var id: String = UUID.randomUUID().toString(),
    @SerializedName("title")
    var title: String = "",
    @SerializedName("description")
    var description: String = "",
    @SerializedName("price")
    var price: String = "",
    var listCreatorId: String = ""
)