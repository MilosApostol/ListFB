package com.example.listfirebase.data.firebasedata.additemsapi

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.UUID

@Entity
data class AddItemsData(
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