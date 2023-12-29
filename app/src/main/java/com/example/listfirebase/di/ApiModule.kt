package com.example.listfirebase.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.example.listfirebase.data.firebasedata.additemsapi.AddItemsRepository
import com.example.listfirebase.data.firebasedata.additemsapi.ApiItemsClient
import com.example.listfirebase.data.firebasedata.additemsapi.ApiService
import com.example.listfirebase.data.firebasedata.listfirebase.ListRepository
import com.example.listfirebase.data.firebasedata.listfirebase.ListViewModel
import com.example.listfirebase.data.firebasedata.items.ItemsRepository
import com.example.listfirebase.data.room.addlist.ListDao
import com.example.listfirebase.data.room.addlist.ListRoomRepository
import com.example.listfirebase.data.room.addlist.ListRoomViewModel
import com.example.listfirebase.data.room.loginregister.UserRepository
import com.example.listfirebase.session.ListSession
import com.example.listfirebase.session.UserSessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun providesAddItemsService(): ApiService {
        return ApiItemsClient.apiService
    }

    @Provides
    @Singleton
    fun providesAddItemsRepository(apiService: ApiService): AddItemsRepository {
        return AddItemsRepository(apiService)
    }


    @Provides
    fun provideConnectivityManager(application: Application): ConnectivityManager {
        return application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
}