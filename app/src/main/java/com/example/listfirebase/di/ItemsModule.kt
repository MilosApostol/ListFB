package com.example.listfirebase.di

import com.example.listfirebase.data.AppDatabase
import com.example.listfirebase.data.firebasedata.items.ItemsRepository
import com.example.listfirebase.data.room.items.ItemsDao
import com.example.listfirebase.data.room.items.ItemsRoomRepository
import com.example.listfirebase.data.room.items.ItemsRoomViewModel
import com.example.listfirebase.session.ListSession
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ItemsModule {

    //room
    @Provides
    @Singleton
    fun providesItemsRoomDao(database: AppDatabase) = database.itemDao()

    @Provides
    @Singleton
    fun providesItemsRoomRepository(dao: ItemsDao): ItemsRoomRepository =
        ItemsRoomRepository(dao)

    @Provides
    @Singleton
    fun providesItemsRoomViewModel(
        repository: ItemsRoomRepository,
        itemsRoomRepository: ItemsRoomRepository,
        listSession: ListSession) =
        ItemsRoomViewModel(repository, listSession)

    //firebase

    @Provides
    @Singleton
    fun providesItemsRepository(dao: ItemsDao): ItemsRepository = ItemsRepository(dao)

}