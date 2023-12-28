package com.example.listfirebase.data.room.loginregister

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "userId")
    var userId: String = UUID.randomUUID().toString(),
    val userEmail: String = "",
    val userPassword: String = "",
    val userHolderId: String = "",
    val isLoggedIn: Boolean = false
) {
}