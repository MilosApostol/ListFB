package com.example.listfirebase.di

import com.example.listfirebase.data.AppDatabase
import com.example.listfirebase.data.firebasedata.additemsapi.AddItemsDao
import com.example.listfirebase.data.firebasedata.additemsapi.AddItemsData
import com.example.listfirebase.data.firebasedata.additemsapi.AddItemsRepository
import com.example.listfirebase.data.firebasedata.additemsapi.AddItemsViewModel
import com.example.listfirebase.data.retrofit.ApiService
import com.example.listfirebase.data.room.additemscustom.AddItemsCustomDao
import com.example.listfirebase.data.room.additemscustom.AddItemsCustomRep
import com.example.listfirebase.data.room.additemscustom.AddItemsCustomViewM
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
class AddItemsModule {

    @Provides
    @Singleton
    fun providesDao(database: AppDatabase) = database.addItemsDao()

    @Provides
    @Singleton
    fun  providesAddItemsRoomRep(dao: AddItemsCustomDao) = AddItemsCustomRep(dao)

    @Provides
    @Singleton
    fun providesViewModelRoom(repository: AddItemsCustomRep) = AddItemsCustomViewM(repository)

    @Provides
    @Singleton
    fun providesFireDao(database: AppDatabase) = database.addItemsFireDao()

    @Provides
    @Singleton
    fun providesViewModel(addItemsRepository: AddItemsRepository) = AddItemsViewModel(addItemsRepository)
}