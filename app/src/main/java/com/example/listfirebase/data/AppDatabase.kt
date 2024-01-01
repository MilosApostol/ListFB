package com.example.listfirebase.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.listfirebase.data.firebasedata.additemsapi.AddItemsData
import com.example.listfirebase.data.firebasedata.items.ItemsEntity
import com.example.listfirebase.data.firebasedata.listfirebase.ListEntity
import com.example.listfirebase.data.room.additems.ItemsDao
import com.example.listfirebase.data.room.additemscustom.AddItemsCustomDao
import com.example.listfirebase.data.room.addlist.ListDao
import com.example.listfirebase.data.room.loginregister.LoginDao
import com.example.listfirebase.data.room.loginregister.UserEntity

@Database(
    entities = [
        UserEntity::class,
        ListEntity::class,
        ItemsEntity::class,
        AddItemsData::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun loginDao(): LoginDao
    abstract fun listDao(): ListDao
    abstract fun itemDao(): ItemsDao
    abstract fun addItemsDao(): AddItemsCustomDao

    companion object {
        val DATABASE = "database"
    }
}